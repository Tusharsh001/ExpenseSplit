// src/components/auth/AuthLayout.jsx
import React from 'react';
import { Link } from 'react-router-dom';
import '../../styles/auth.css';

const AuthLayout = ({ children, title, subtitle }) => {
    return (
        <div className="auth-container">
            <div className="auth-grid">
                {/* Left Side - Branding */}
                <div className="auth-brand">
                    <div className="brand-content">
                        <h1 className="brand-logo">SplitExpense</h1>
                        <h2 className="brand-tagline">Split expenses with friends, easily</h2>
                        <div className="brand-features">
                            <div className="feature-item">
                                <span className="feature-icon">💰</span>
                                <span>Track shared expenses</span>
                            </div>
                            <div className="feature-item">
                                <span className="feature-icon">👥</span>
                                <span>Split bills equally or by percentage</span>
                            </div>
                            <div className="feature-item">
                                <span className="feature-icon">🔄</span>
                                <span>Settle up with friends</span>
                            </div>
                        </div>
                    </div>
                </div>

                {/* Right Side - Auth Form */}
                <div className="auth-form-container">
                    <div className="auth-card">
                        <div className="auth-header">
                            <h2 className="auth-title">{title}</h2>
                            {subtitle && <p className="auth-subtitle">{subtitle}</p>}
                        </div>
                        {children}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default AuthLayout;