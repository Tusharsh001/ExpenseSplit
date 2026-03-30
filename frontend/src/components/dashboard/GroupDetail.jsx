// src/components/dashboard/GroupDetail.jsx
import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import LoadingSpinner from '../common/LoadingSpinner';
import ErrorAlert from '../common/ErrorAlert';
import { groupAPI, expenseAPI } from '../../services/api';
import '../../styles/dashboard.css';

const GroupDetail = () => {
    const { groupId } = useParams();
    const navigate = useNavigate();
    
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [group, setGroup] = useState(null);
    const [expenses, setExpenses] = useState([]);
    const [simplifyDebts, setSimplifyDebts] = useState(true);
    const [showAddExpense, setShowAddExpense] = useState(false);

    useEffect(() => {
        fetchGroupDetails();
    }, [groupId]);

    const fetchGroupDetails = async () => {
        setLoading(true);
        setError(null);
        
        try {
            // Fetch group details
            const groupResponse = await groupAPI.getGroupById(groupId);
            setGroup(groupResponse.data);
            
            // Fetch group expenses
            const expensesResponse = await expenseAPI.getGroupExpenses(groupId);
            setExpenses(expensesResponse.data || []);
            
        } catch (err) {
            console.error('Error fetching group details:', err);
            setError('Failed to load group details. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    const handleAddExpense = () => {
        navigate(`/expenses/add?groupId=${groupId}`);
    };

    const handleSettleUp = () => {
        navigate(`/groups/${groupId}/settle`);
    };

    const handleMemberClick = (userId) => {
        navigate(`/users/${userId}`);
    };

    const calculateMemberBalance = (memberId) => {
        // Calculate balance from expenses
        let balance = 0;
        
        expenses.forEach(expense => {
            if (expense.paidBy?.id === memberId) {
                balance += expense.amount;
            }
            
            const split = expense.splits?.find(s => s.userId === memberId);
            if (split) {
                balance -= split.amount;
            }
        });
        
        return balance;
    };

    if (loading) {
        return (
            <div className="group-detail-container">
                <LoadingSpinner message="Loading group details..." />
            </div>
        );
    }

    if (error || !group) {
        return (
            <div className="group-detail-container">
                <ErrorAlert 
                    message={error || 'Group not found'} 
                    onRetry={fetchGroupDetails}
                />
                <button 
                    className="back-btn"
                    onClick={() => navigate('/dashboard')}
                >
                    ← Back to Dashboard
                </button>
            </div>
        );
    }

    const currentUser = localStorage.getItem('username');
    const currentUserBalance = calculateMemberBalance(
        group.members?.find(m => m.username === currentUser)?.id
    );

    return (
        <div className="group-detail-container">
            {/* Header */}
            <header className="detail-header">
                <button className="back-btn" onClick={() => navigate('/dashboard')}>
                    ←
                </button>
                <div className="group-title">
                    <h1>{group.name}</h1>
                    <span className="member-count">
                        {group.memberCount || group.members?.length} people
                    </span>
                </div>
                <div className="header-actions">
                    <button 
                        className="btn btn-primary"
                        onClick={handleAddExpense}
                    >
                        <span className="btn-icon">+</span>
                        Add an expense
                    </button>
                    <button 
                        className="btn btn-secondary"
                        onClick={handleSettleUp}
                    >
                        <span className="btn-icon">💰</span>
                        Settle up
                    </button>
                </div>
            </header>

            <div className="group-content">
                {/* Balance Status */}
                <div className="balance-status">
                    <span className={`status-message ${
                        currentUserBalance === 0 ? 'settled' : 
                        currentUserBalance > 0 ? 'positive' : 'negative'
                    }`}>
                        {currentUserBalance === 0 && 'You are all settled up in this group.'}
                        {currentUserBalance > 0 && `You are owed ₹${currentUserBalance.toFixed(2)} in this group.`}
                        {currentUserBalance < 0 && `You owe ₹${Math.abs(currentUserBalance).toFixed(2)} in this group.`}
                    </span>
                    <button className="show-settled-btn">
                        Show settled expenses →
                    </button>
                </div>

                {/* Group Balances */}
                <div className="balances-section">
                    <div className="section-header">
                        <h2>{group.name}</h2>
                        <div className="simplify-toggle">
                            <span className="toggle-label">GROUP BALANCES</span>
                            <label className="switch">
                                <input 
                                    type="checkbox" 
                                    checked={simplifyDebts}
                                    onChange={() => setSimplifyDebts(!simplifyDebts)}
                                />
                                <span className="slider round"></span>
                            </label>
                            <span className="toggle-text">
                                Simplify debts is {simplifyDebts ? 'ON' : 'OFF'} 🔒
                            </span>
                        </div>
                    </div>

                    <div className="member-list">
                        {group.members?.map(member => {
                            const balance = calculateMemberBalance(member.id);
                            const isCurrentUser = member.username === currentUser;
                            
                            return (
                                <div 
                                    key={member.id} 
                                    className={`member-item ${isCurrentUser ? 'current-user' : ''}`}
                                    onClick={() => handleMemberClick(member.id)}
                                >
                                    <div className="member-info">
                                        <span className="member-name">
                                            {member.username}
                                            {isCurrentUser && ' (you)'}
                                        </span>
                                        <span className={`member-status ${
                                            balance === 0 ? 'settled' :
                                            balance > 0 ? 'positive' : 'negative'
                                        }`}>
                                            {balance === 0 && 'settled up'}
                                            {balance > 0 && `gets back ₹${balance.toFixed(2)}`}
                                            {balance < 0 && `owes ₹${Math.abs(balance).toFixed(2)}`}
                                        </span>
                                    </div>
                                    <button className="view-details-btn">
                                        View details »
                                    </button>
                                </div>
                            );
                        })}
                    </div>
                </div>

                {/* Recent Expenses */}
                {expenses.length > 0 && (
                    <div className="recent-expenses-section">
                        <h3>Recent Expenses</h3>
                        <div className="expense-list">
                            {expenses.slice(0, 5).map(expense => (
                                <div 
                                    key={expense.id} 
                                    className="expense-item"
                                    onClick={() => navigate(`/expenses/${expense.id}`)}
                                >
                                    <div className="expense-info">
                                        <span className="expense-desc">
                                            {expense.description}
                                        </span>
                                        <span className="expense-meta">
                                            Paid by {expense.paidBy?.username}
                                        </span>
                                    </div>
                                    <span className="expense-amount">
                                        ₹{expense.amount}
                                    </span>
                                </div>
                            ))}
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
};

export default GroupDetail;