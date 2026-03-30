// src/components/landing/CTA.jsx
import React from 'react';
import { useNavigate } from 'react-router-dom';

const CTA = () => {
    const navigate = useNavigate();

    return (
        <section id="cta" className="simple-cta">
            <h2>Ready to simplify shared expenses?</h2>
            <button className="btn-primary large" onClick={() => navigate('/signup')}>
                Create your free account
            </button>
            <p className="cta-login">
                Already have an account?{' '}
                <button className="text-link" onClick={() => navigate('/login')}>
                    Sign in
                </button>
            </p>
        </section>
    );
};

export default CTA;