// src/components/landing/Navbar.jsx
import React from 'react';
import { useNavigate } from 'react-router-dom';

const Navbar = () => {
    const navigate = useNavigate();

    const scrollToSection = (sectionId) => {
        const section = document.getElementById(sectionId);
        if (section) {
            section.scrollIntoView({ behavior: 'smooth' });
        }
    };

    return (
        <nav className="landing-nav">
            <div className="nav-container">
                <div className="logo" onClick={() => window.scrollTo({ top: 0, behavior: 'smooth' })}>
                    <span className="logo-icon">💰</span>
                    <span className="logo-text">SplitExpense</span>
                </div>

                <div className="nav-menu">
                    <button onClick={() => scrollToSection('features')} className="nav-link">Features</button>
                    <button onClick={() => scrollToSection('how-it-works')} className="nav-link">How it Works</button>
                    <button onClick={() => scrollToSection('pricing')} className="nav-link">Pricing</button>
                </div>

                <div className="nav-actions">
                    <button className="btn-text" onClick={() => navigate('/login')}>Log in</button>
                    <button className="btn-primary small" onClick={() => navigate('/signup')}>Sign up free</button>
                </div>
            </div>
        </nav>
    );
};

export default Navbar;