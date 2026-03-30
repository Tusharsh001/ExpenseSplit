// src/components/landing/LandingPage.jsx
import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../../styles/landing.css';

const LandingPage = () => {
    const navigate = useNavigate();

    return (
        <div className="landing">
            {/* Simple Navigation */}
            <nav className="landing-nav">
                <div className="nav-container">
                    <div className="logo">
                        <span className="logo-icon">💰</span>
                        <span className="logo-text">SplitExpense</span>
                    </div>
                    <div className="nav-actions">
                        <button className="btn-text" onClick={() => navigate('/login')}>Login</button>
                        <button className="btn-primary small" onClick={() => navigate('/signup')}>Sign Up</button>
                    </div>
                </div>
            </nav>

            {/* Main Hero Section */}
            <main className="landing-main">
                <div className="hero">
                    <h1 className="hero-title">
                        Split expenses with <span className="highlight">friends</span>
                    </h1>
                    <p className="hero-subtitle">
                        Track group expenses, settle debts, and never fight about money again.
                    </p>
                    <div className="hero-actions">
                        <button className="btn-primary large" onClick={() => navigate('/signup')}>
                            Get Started — It's free
                        </button>
                        <button className="btn-outline large" onClick={() => navigate('/demo')}>
                            Watch Demo
                        </button>
                    </div>
                    <p className="hero-stats">
                        <span>✨ 50K+ happy users</span>
                        <span className="dot">•</span>
                        <span>💰 ₹10M+ expenses tracked</span>
                    </p>
                </div>

                {/* Simple Features Grid */}
                <div className="features">
                    <div className="feature-card">
                        <div className="feature-icon">🔄</div>
                        <h3>Equal Split</h3>
                        <p>Split bills equally among friends</p>
                    </div>
                    <div className="feature-card">
                        <div className="feature-icon">📊</div>
                        <h3>Track Balances</h3>
                        <p>See who owes what at a glance</p>
                    </div>
                    <div className="feature-card">
                        <div className="feature-icon">💳</div>
                        <h3>Settle Up</h3>
                        <p>Easy settlement suggestions</p>
                    </div>
                </div>

                {/* Simple CTA */}
                <div className="simple-cta">
                    <h2>Ready to simplify shared expenses?</h2>
                    <button className="btn-primary large" onClick={() => navigate('/signup')}>
                        Create your free account
                    </button>
                </div>
            </main>

            {/* Simple Footer */}
            <footer className="landing-footer">
                <div className="footer-content">
                    <p>© 2026 SplitExpense. All rights reserved.</p>
                    <div className="footer-links">
                        <button className="footer-link" onClick={() => navigate('/privacy')}>Privacy</button>
                        <button className="footer-link" onClick={() => navigate('/terms')}>Terms</button>
                        <button className="footer-link" onClick={() => navigate('/contact')}>Contact</button>
                    </div>
                </div>
            </footer>
        </div>
    );
};

export default LandingPage;