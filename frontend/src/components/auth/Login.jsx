// src/components/auth/Login.jsx
import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import AuthLayout from './AuthLayout';
import InputField from '../common/InputField';
import authService from '../../services/auth';
import '../../styles/auth.css';

const Login = () => {
    console.log("🔵 Login component rendered");
    
    const navigate = useNavigate();
    const [formData, setFormData] = useState({
        username: '',
        password: ''
    });
    const [errors, setErrors] = useState({});
    const [isLoading, setIsLoading] = useState(false);

    console.log("🔵 Current form state:", formData);

    const handleChange = (e) => {
        console.log("🔵 Input changed:", e.target.name, e.target.value);
        setFormData({
            ...formData,
            [e.target.name]: e.target.value
        });
        if (errors[e.target.name]) {
            setErrors({
                ...errors,
                [e.target.name]: ''
            });
        }
    };

    const validateForm = () => {
        const newErrors = {};
        if (!formData.username) {
            newErrors.username = 'Username is required';
        }
        if (!formData.password) {
            newErrors.password = 'Password is required';
        }
        return newErrors;
    };

    const handleSubmit = async (e) => {
        console.log("🔵 FORM SUBMITTED!");
        e.preventDefault();
        
        console.log("🔵 Validation starting...");
        const newErrors = validateForm();
        console.log("🔵 Validation errors:", newErrors);
        
        if (Object.keys(newErrors).length > 0) {
            setErrors(newErrors);
            return;
        }

        setIsLoading(true);
        console.log("🔵 Loading state set to true");
        
        try {
            console.log('📤 Sending login request with username:', formData.username);
            
            const response = await authService.login(formData.username, formData.password);
            
            console.log('✅ Login response received:', response);
            console.log('🔑 Token stored:', localStorage.getItem('token'));
            console.log('👤 Username stored:', localStorage.getItem('username'));
            
            // Check if authentication worked
            const isAuth = authService.isAuthenticated();
            console.log('🔐 Is authenticated:', isAuth);
            
            if (isAuth) {
                console.log('➡️ Redirecting to dashboard...');
                navigate('/dashboard');
            } else {
                console.log('⚠️ Not authenticated after login');
                setErrors({ general: 'Authentication failed' });
            }
            
        } catch (error) {
            console.error('❌ Login error:', error);
            setErrors({
                general: error.message || 'Invalid username or password'
            });
        } finally {
            console.log("🔵 Loading state set to false");
            setIsLoading(false);
        }
    };

    // Test function to verify console is working
    const testConsole = () => {
        console.log("🟢 TEST: Console is working!");
        console.log("Current username:", formData.username);
        console.log("Current password:", formData.password ? "[HIDDEN]" : "empty");
    };

    return (
        <AuthLayout title="Welcome back!" subtitle="Login to continue">
            <form onSubmit={(e) => {
                console.log("🔵 Form onSubmit event triggered!");
                handleSubmit(e);
            }} className="auth-form">
                
                {/* Temporary test button - remove later */}
                <button 
                    type="button" 
                    onClick={testConsole} 
                    style={{
                        marginBottom: '15px', 
                        padding: '8px',
                        background: '#4CAF50', 
                        color: 'white',
                        border: 'none',
                        borderRadius: '4px',
                        cursor: 'pointer'
                    }}
                >
                    🧪 Test Console
                </button>

                {errors.general && (
                    <div className="alert alert-error">
                        <span className="alert-icon">⚠️</span>
                        {errors.general}
                    </div>
                )}

                <InputField
                    type="text"
                    name="username"
                    placeholder="Username"
                    value={formData.username}
                    onChange={handleChange}
                    error={errors.username}
                    icon="👤"
                    autoFocus
                />

                <InputField
                    type="password"
                    name="password"
                    placeholder="Password"
                    value={formData.password}
                    onChange={handleChange}
                    error={errors.password}
                    icon="🔒"
                />

                <button 
                    type="submit" 
                    className="auth-button primary" 
                    disabled={isLoading}
                >
                    {isLoading ? 'Logging in...' : 'Login'}
                </button>

                <div className="auth-links">
                    <Link to="/forgot-password" className="auth-link-small">Forgot password?</Link>
                </div>

                <p className="auth-footer">
                    Don't have an account?{' '}
                    <Link to="/signup" className="auth-link">Sign up</Link>
                </p>
            </form>
        </AuthLayout>
    );
};

export default Login;