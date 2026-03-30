// src/components/dashboard/Sidebar.jsx
import React from 'react';
import { useNavigate } from 'react-router-dom';
import LoadingSpinner from '../common/LoadingSpinner';

const Sidebar = ({ groups, friends, onSelectGroup, loading = false }) => {
    const navigate = useNavigate();
    const currentUser = localStorage.getItem('username');

    const calculateGroupBalance = (group) => {
        // Extract numeric balance from string like "You owe ₹500" or "You are owed ₹1,200"
        const balanceStr = group.userBalance || '';
        const match = balanceStr.match(/[+-]?\d+(\.\d+)?/);
        return match ? parseFloat(match[0]) : 0;
    };

    if (loading) {
        return (
            <aside className="sidebar">
                <div className="sidebar-header">
                    <h1 className="app-logo">Splittwise</h1>
                </div>
                <div className="sidebar-loading">
                    <LoadingSpinner size="small" />
                </div>
            </aside>
        );
    }

    return (
        <aside className="sidebar">
            <div className="sidebar-header">
                <h1 className="app-logo">Splittwise</h1>
                <div className="user-info">
                    <span className="user-name">{currentUser}</span>
                </div>
            </div>

            <div className="sidebar-section">
                <div className="section-header">
                    <h3 className="section-label">All expenses</h3>
                </div>
                
                {/* Groups Section */}
                <div className="nav-group">
                    <div className="nav-header">
                        <span>GROUPS</span>
                        <button 
                            className="add-button"
                            onClick={() => navigate('/groups/create')}
                        >
                            +
                        </button>
                    </div>
                    
                    {groups.length > 0 ? (
                        groups.map(group => {
                            const balance = calculateGroupBalance(group);
                            return (
                                <div 
                                    key={group.id} 
                                    className={`nav-item ${balance !== 0 ? 'has-balance' : ''}`}
                                    onClick={() => onSelectGroup(group)}
                                >
                                    <span className="nav-icon">👥</span>
                                    <span className="nav-text">{group.name}</span>
                                    <span className={`nav-balance ${
                                        balance > 0 ? 'positive' : balance < 0 ? 'negative' : 'settled'
                                    }`}>
                                        {balance > 0 && 'owes you'}
                                        {balance < 0 && 'you owe'}
                                        {balance === 0 && 'settled'}
                                    </span>
                                </div>
                            );
                        })
                    ) : (
                        <div className="empty-nav">
                            <p>No groups yet</p>
                            <button 
                                className="btn-link"
                                onClick={() => navigate('/groups/create')}
                            >
                                Create a group
                            </button>
                        </div>
                    )}
                </div>

                {/* Friends Section */}
                <div className="nav-group">
                    <div className="nav-header">
                        <span>FRIENDS</span>
                    </div>
                    
                    {friends.length > 0 ? (
                        friends.map(friend => (
                            <div key={friend.id} className="nav-item">
                                <span className="nav-icon">👤</span>
                                <span className="nav-text">{friend.username}</span>
                                <span className={`nav-balance ${
                                    friend.balance > 0 ? 'positive' : 
                                    friend.balance < 0 ? 'negative' : 'settled'
                                }`}>
                                    {friend.balance > 0 && 'owes you'}
                                    {friend.balance < 0 && 'you owe'}
                                    {friend.balance === 0 && 'settled'}
                                </span>
                            </div>
                        ))
                    ) : (
                        <div className="empty-nav">
                            <p>No friends yet</p>
                            <p className="hint">Invite friends to get started</p>
                        </div>
                    )}
                </div>
            </div>

            {/* Invite Section */}
            <div className="sidebar-footer">
                <button 
                    className="invite-button"
                    onClick={() => navigate('/invite')}
                >
                    <span className="invite-icon">✉️</span>
                    Invite friends
                </button>
            </div>
        </aside>
    );
};

export default Sidebar;