// src/components/dashboard/FriendItem.jsx
import React from 'react';
import { useNavigate } from 'react-router-dom';

const FriendItem = ({ friend, showBalance = true, showActions = false }) => {
    const navigate = useNavigate();

    const getBalanceText = () => {
        if (friend.balance === 0) return { text: 'settled up', className: 'settled' };
        if (friend.balance > 0) return { text: `owes you ₹${friend.balance.toFixed(2)}`, className: 'positive' };
        return { text: `you owe ₹${Math.abs(friend.balance).toFixed(2)}`, className: 'negative' };
    };

    const balance = getBalanceText();

    const handleSettleUp = (e) => {
        e.stopPropagation();
        navigate(`/settle?friend=${friend.id}`);
    };

    const handleAddExpense = (e) => {
        e.stopPropagation();
        navigate(`/expenses/add?friend=${friend.id}`);
    };

    return (
        <div 
            className={`friend-item ${balance.className}`}
            onClick={() => navigate(`/friends/${friend.id}`)}
        >
            <div className="friend-avatar">
                {friend.profilePicture ? (
                    <img src={friend.profilePicture} alt={friend.username} />
                ) : (
                    <div className="avatar-placeholder">
                        {friend.username?.charAt(0).toUpperCase()}
                    </div>
                )}
            </div>

            <div className="friend-info">
                <span className="friend-name">{friend.username}</span>
                {friend.email && (
                    <span className="friend-email">{friend.email}</span>
                )}
                {showBalance && (
                    <span className={`friend-balance ${balance.className}`}>
                        {balance.text}
                    </span>
                )}
            </div>

            {showActions && (
                <div className="friend-actions">
                    <button 
                        className="action-btn settle"
                        onClick={handleSettleUp}
                        title="Settle up"
                    >
                        💰
                    </button>
                    <button 
                        className="action-btn add"
                        onClick={handleAddExpense}
                        title="Add expense"
                    >
                        +
                    </button>
                </div>
            )}
        </div>
    );
};

export default FriendItem;