// src/components/dashboard/ExpenseItem.jsx
import React from 'react';
import { useNavigate } from 'react-router-dom';

const ExpenseItem = ({ expense, showGroup = false }) => {
    const navigate = useNavigate();
    const currentUser = localStorage.getItem('username');

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        const now = new Date();
        const diffTime = Math.abs(now - date);
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
        
        if (diffDays === 0) return 'Today';
        if (diffDays === 1) return 'Yesterday';
        if (diffDays < 7) return `${diffDays} days ago`;
        return date.toLocaleDateString('en-US', { month: 'short', day: 'numeric' });
    };

    const getYourShare = () => {
        if (!expense.splits) return 0;
        const yourSplit = expense.splits.find(
            split => split.username === currentUser || split.userId?.toString() === localStorage.getItem('userId')
        );
        return yourSplit?.amount || 0;
    };

    const yourShare = getYourShare();
    const isPaidByYou = expense.paidBy?.username === currentUser;
    const date = formatDate(expense.createdAt);

    return (
        <div 
            className="expense-item" 
            onClick={() => navigate(`/expenses/${expense.id}`)}
        >
            <div className="expense-icon">
                {expense.splitType === 'EQUAL' ? '🔄' : '💰'}
            </div>
            
            <div className="expense-details">
                <div className="expense-main">
                    <span className="expense-description">{expense.description}</span>
                    <span className="expense-amount">₹{expense.amount.toFixed(2)}</span>
                </div>
                
                <div className="expense-meta">
                    <span className="expense-payer">
                        {isPaidByYou ? 'You paid' : `${expense.paidBy?.username} paid`}
                    </span>
                    <span className="expense-date">{date}</span>
                </div>

                {showGroup && expense.group && (
                    <div className="expense-group">
                        in {expense.group.name}
                    </div>
                )}

                <div className={`expense-share ${yourShare > 0 ? 'owes' : 'lent'}`}>
                    {yourShare > 0 
                        ? `You owe ₹${yourShare.toFixed(2)}` 
                        : `You lent ₹${Math.abs(yourShare).toFixed(2)}`}
                </div>
            </div>

            <div className="expense-status">
                {expense.status === 'pending' ? (
                    <span className="status-badge pending">Pending</span>
                ) : (
                    <span className="status-badge settled">Settled</span>
                )}
            </div>
        </div>
    );
};

export default ExpenseItem;