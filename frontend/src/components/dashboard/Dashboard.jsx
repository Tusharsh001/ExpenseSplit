// src/components/dashboard/Dashboard.jsx
import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Sidebar from './Sidebar';
import GroupDetail from './GroupDetail';
import ExpenseItem from './ExpenseItem';
import FriendItem from './FriendItem';
import LoadingSpinner from '../common/LoadingSpinner';
import ErrorAlert from '../common/ErrorAlert';
import { groupAPI, expenseAPI } from '../../services/api';
import '../../styles/dashboard.css';

const Dashboard = () => {
    const navigate = useNavigate();
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [selectedGroup, setSelectedGroup] = useState(null);
    const [showInviteModal, setShowInviteModal] = useState(false);
    const [inviteEmail, setInviteEmail] = useState('');
    
    // State for data from backend
    const [groups, setGroups] = useState([]);
    const [friends, setFriends] = useState([]);
    const [recentExpenses, setRecentExpenses] = useState([]);
    const [dashboardData, setDashboardData] = useState({
        totalOwed: 0,
        totalOwes: 0,
        totalGroups: 0,
        totalFriends: 0,
        pendingExpenses: 0
    });

    // Fetch all dashboard data on component mount
    useEffect(() => {
        fetchDashboardData();
    }, []);

    const fetchDashboardData = async () => {
        setLoading(true);
        setError(null);
        
        try {
            // Fetch user's groups
            const groupsResponse = await groupAPI.getMyGroups();
            const userGroups = groupsResponse.data || [];
            
            // Extract unique friends from all groups (excluding current user)
            const currentUsername = localStorage.getItem('username');
            const allMembers = userGroups.flatMap(group => group.members || []);
            const uniqueFriends = Array.from(
                new Map(allMembers.map(m => [m.id, m])).values()
            ).filter(m => m.username !== currentUsername);
            
            // Fetch recent expenses
            let userExpenses = [];
            try {
                const expensesResponse = await expenseAPI.getMyExpenses();
                userExpenses = expensesResponse.data || [];
            } catch (err) {
                console.log('No expenses yet or error fetching expenses');
            }
            
            setGroups(userGroups);
            setFriends(uniqueFriends);
            setRecentExpenses(userExpenses.slice(0, 10)); // Last 10 expenses
            
            // Build dashboard summary
            buildDashboardSummary(userGroups, uniqueFriends, userExpenses);
            
        } catch (err) {
            console.error('Error fetching dashboard data:', err);
            setError('Failed to load dashboard data. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    const buildDashboardSummary = (groups, friends, expenses) => {
        // Calculate total owed to user and total user owes
        let totalOwed = 0;
        let totalOwes = 0;
        
        groups.forEach(group => {
            const balanceStr = group.userBalance || '0';
            const match = balanceStr.match(/[+-]?\d+(\.\d+)?/);
            const balance = match ? parseFloat(match[0]) : 0;
            
            if (balance > 0) totalOwed += balance;
            if (balance < 0) totalOwes += Math.abs(balance);
        });
        
        // Count pending expenses
        const pendingCount = expenses.filter(e => e.status === 'pending').length;
        
        setDashboardData({
            totalOwed,
            totalOwes,
            totalGroups: groups.length,
            totalFriends: friends.length,
            pendingExpenses: pendingCount
        });
    };

    const handleInvite = async (e) => {
        e.preventDefault();
        try {
            // You'll need to implement this endpoint in your backend
            // await groupAPI.inviteFriend({ email: inviteEmail });
            alert(`Invitation sent to ${inviteEmail}`);
            setInviteEmail('');
            setShowInviteModal(false);
        } catch (err) {
            setError('Failed to send invite. Please try again.');
        }
    };

    const handleGroupClick = (group) => {
        setSelectedGroup(group);
        navigate(`/groups/${group.id}`);
    };

    const handleAddExpense = () => {
        navigate('/expenses/add');
    };

    const handleSettleUp = () => {
        navigate('/settle');
    };

    const handleViewAllExpenses = () => {
        navigate('/expenses');
    };

    const handleViewAllFriends = () => {
        navigate('/friends');
    };

    // If a group is selected, show group detail view
    if (selectedGroup) {
        return <GroupDetail 
            group={selectedGroup} 
            onBack={() => setSelectedGroup(null)} 
        />;
    }

    // Loading state
    if (loading) {
        return (
            <div className="dashboard-container">
                <Sidebar 
                    groups={[]} 
                    friends={[]}
                    onSelectGroup={() => {}}
                    loading={true}
                />
                <main className="main-content">
                    <LoadingSpinner message="Loading your dashboard..." />
                </main>
            </div>
        );
    }

    // Error state
    if (error) {
        return (
            <div className="dashboard-container">
                <Sidebar 
                    groups={groups} 
                    friends={friends}
                    onSelectGroup={handleGroupClick}
                />
                <main className="main-content">
                    <ErrorAlert message={error} onRetry={fetchDashboardData} />
                </main>
            </div>
        );
    }

    return (
        <div className="dashboard-container">
            <Sidebar 
                groups={groups} 
                friends={friends}
                onSelectGroup={handleGroupClick}
            />
            
            <main className="main-content">
                {/* Welcome Header */}
                <div className="welcome-header">
                    <h1 className="page-title">
                        Welcome back, {localStorage.getItem('username')}!
                    </h1>
                    <p className="page-subtitle">
                        Here's what's happening with your money
                    </p>
                </div>

                {/* Summary Cards */}
                <div className="summary-cards">
                    <div className="summary-card">
                        <div className="card-icon owed">💰</div>
                        <div className="card-content">
                            <span className="card-label">You're owed</span>
                            <span className="card-value positive">
                                ₹{dashboardData.totalOwed.toFixed(2)}
                            </span>
                        </div>
                    </div>
                    
                    <div className="summary-card">
                        <div className="card-icon owes">💸</div>
                        <div className="card-content">
                            <span className="card-label">You owe</span>
                            <span className="card-value negative">
                                ₹{dashboardData.totalOwes.toFixed(2)}
                            </span>
                        </div>
                    </div>
                    
                    <div className="summary-card">
                        <div className="card-icon groups">👥</div>
                        <div className="card-content">
                            <span className="card-label">Active Groups</span>
                            <span className="card-value">
                                {dashboardData.totalGroups}
                            </span>
                        </div>
                    </div>
                    
                    <div className="summary-card">
                        <div className="card-icon pending">⏳</div>
                        <div className="card-content">
                            <span className="card-label">Pending</span>
                            <span className="card-value">
                                {dashboardData.pendingExpenses}
                            </span>
                        </div>
                    </div>
                </div>

                {/* Quick Actions */}
                <div className="quick-actions">
                    <button 
                        className="action-button primary"
                        onClick={handleAddExpense}
                    >
                        <span className="button-icon">+</span>
                        Add Expense
                    </button>
                    <button 
                        className="action-button secondary"
                        onClick={handleSettleUp}
                    >
                        <span className="button-icon">💰</span>
                        Settle Up
                    </button>
                    <button 
                        className="action-button secondary"
                        onClick={() => setShowInviteModal(true)}
                    >
                        <span className="button-icon">✉️</span>
                        Invite Friends
                    </button>
                </div>

                {/* Two Column Layout */}
                <div className="dashboard-grid">
                    {/* Left Column - Recent Expenses */}
                    <div className="grid-column">
                        <div className="section-header">
                            <h2 className="section-title">Recent Expenses</h2>
                            {recentExpenses.length > 0 && (
                                <button 
                                    className="view-all-link"
                                    onClick={handleViewAllExpenses}
                                >
                                    View all →
                                </button>
                            )}
                        </div>
                        
                        <div className="expense-list">
                            {recentExpenses.length > 0 ? (
                                recentExpenses.map(expense => (
                                    <ExpenseItem 
                                        key={expense.id} 
                                        expense={expense}
                                        showGroup={true}
                                    />
                                ))
                            ) : (
                                <div className="empty-state">
                                    <div className="empty-icon">📝</div>
                                    <p className="empty-message">No expenses yet</p>
                                    <button 
                                        className="empty-action"
                                        onClick={handleAddExpense}
                                    >
                                        Add your first expense
                                    </button>
                                </div>
                            )}
                        </div>
                    </div>

                    {/* Right Column - Friends & Groups */}
                    <div className="grid-column">
                        {/* Friends Section */}
                        <div className="section-header">
                            <h2 className="section-title">Friends</h2>
                            {friends.length > 0 && (
                                <button 
                                    className="view-all-link"
                                    onClick={handleViewAllFriends}
                                >
                                    View all →
                                </button>
                            )}
                        </div>
                        
                        <div className="friend-list">
                            {friends.length > 0 ? (
                                friends.slice(0, 5).map(friend => (
                                    <FriendItem 
                                        key={friend.id} 
                                        friend={friend}
                                        showBalance={true}
                                        showActions={true}
                                    />
                                ))
                            ) : (
                                <div className="empty-state small">
                                    <div className="empty-icon">👥</div>
                                    <p className="empty-message">No friends yet</p>
                                    <button 
                                        className="empty-action"
                                        onClick={() => setShowInviteModal(true)}
                                    >
                                        Invite friends
                                    </button>
                                </div>
                            )}
                        </div>

                        {/* Your Groups Summary */}
                        {groups.length > 0 && (
                            <div className="groups-summary">
                                <h3 className="summary-title">Your Groups</h3>
                                <div className="group-chips">
                                    {groups.slice(0, 3).map(group => (
                                        <button
                                            key={group.id}
                                            className="group-chip"
                                            onClick={() => handleGroupClick(group)}
                                        >
                                            <span className="chip-name">{group.name}</span>
                                            <span className={`chip-balance ${
                                                parseFloat(group.userBalance?.replace(/[^0-9.-]/g, '') || 0) > 0 
                                                    ? 'positive' : 'negative'
                                            }`}>
                                                {group.userBalance}
                                            </span>
                                        </button>
                                    ))}
                                    {groups.length > 3 && (
                                        <button 
                                            className="group-chip more"
                                            onClick={() => navigate('/groups')}
                                        >
                                            +{groups.length - 3} more
                                        </button>
                                    )}
                                </div>
                            </div>
                        )}
                    </div>
                </div>

                {/* Invite Modal */}
                {showInviteModal && (
                    <div className="modal-overlay">
                        <div className="modal">
                            <div className="modal-header">
                                <h3>Invite Friends</h3>
                                <button 
                                    className="close-button"
                                    onClick={() => setShowInviteModal(false)}
                                >
                                    ×
                                </button>
                            </div>
                            
                            <form onSubmit={handleInvite} className="modal-form">
                                <div className="form-group">
                                    <label htmlFor="email">Email address</label>
                                    <input
                                        id="email"
                                        type="email"
                                        placeholder="friend@example.com"
                                        value={inviteEmail}
                                        onChange={(e) => setInviteEmail(e.target.value)}
                                        required
                                        autoFocus
                                    />
                                    <p className="input-hint">
                                        We'll send an invitation email to your friend
                                    </p>
                                </div>
                                
                                <div className="modal-actions">
                                    <button 
                                        type="button" 
                                        className="btn-secondary"
                                        onClick={() => setShowInviteModal(false)}
                                    >
                                        Cancel
                                    </button>
                                    <button type="submit" className="btn-primary">
                                        Send Invitation
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                )}
            </main>
        </div>
    );
};

export default Dashboard;