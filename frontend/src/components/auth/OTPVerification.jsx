// src/components/auth/OTPVerification.jsx
import React, { useState, useEffect, useRef } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import AuthLayout from './AuthLayout';
import authService from '../../services/auth';
import '../../styles/auth.css';

const OTPVerification = () => {
    const navigate = useNavigate();
    const location = useLocation();
    const { email } = location.state || { email: '' };
    
    const [otp, setOtp] = useState(['', '', '', '', '', '']);
    const [timer, setTimer] = useState(60);
    const [canResend, setCanResend] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState('');
    
    const inputRefs = useRef([]);

    useEffect(() => {
        if (inputRefs.current[0]) inputRefs.current[0].focus();
    }, []);

    useEffect(() => {
        let interval;
        if (timer > 0 && !canResend) {
            interval = setInterval(() => setTimer(prev => prev - 1), 1000);
        } else if (timer === 0) {
            setCanResend(true);
        }
        return () => clearInterval(interval);
    }, [timer, canResend]);

    const handleChange = (index, value) => {
        if (value.length > 1) return;
        const newOtp = [...otp];
        newOtp[index] = value;
        setOtp(newOtp);

        if (value !== '' && index < 5) {
            inputRefs.current[index + 1].focus();
        }
    };

    const handleKeyDown = (index, e) => {
        if (e.key === 'Backspace' && otp[index] === '' && index > 0) {
            inputRefs.current[index - 1].focus();
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const otpString = otp.join('');
        if (otpString.length !== 6) {
            setError('Please enter complete OTP');
            return;
        }

        setIsLoading(true);
        try {
            await authService.verifyOTP(email, otpString);
            navigate('/dashboard');
        } catch (error) {
            setError(error.message || 'Invalid OTP');
        } finally {
            setIsLoading(false);
        }
    };

    const handleResend = async () => {
        setCanResend(false);
        setTimer(60);
        try {
            await authService.resendOTP(email);
        } catch (error) {
            setError('Failed to resend OTP');
        }
    };

    return (
        <AuthLayout title="Verify email" subtitle={`Code sent to ${email}`}>
            <form onSubmit={handleSubmit} className="auth-form">
                {error && <div className="alert alert-error">{error}</div>}

                <div className="otp-container">
                    {otp.map((digit, index) => (
                        <input
                            key={index}
                            ref={el => inputRefs.current[index] = el}
                            type="text"
                            maxLength="1"
                            value={digit}
                            onChange={e => handleChange(index, e.target.value)}
                            onKeyDown={e => handleKeyDown(index, e)}
                            className="otp-input"
                            disabled={isLoading}
                        />
                    ))}
                </div>

                <button type="submit" className="auth-button primary" disabled={isLoading}>
                    {isLoading ? 'Verifying...' : 'Verify'}
                </button>

                <div className="resend-section">
                    {canResend ? (
                        <button type="button" className="resend-button" onClick={handleResend}>
                            Resend code
                        </button>
                    ) : (
                        <p className="resend-timer">Resend in {timer}s</p>
                    )}
                </div>
            </form>
        </AuthLayout>
    );
};

export default OTPVerification;