// src/services/auth.js
import api from './api';

class AuthService {
    
    // Store JWT token and user data
    setAuthData(token, username, userData = {}) {
        localStorage.setItem('token', token);
        localStorage.setItem('username', username);
        if (userData.email) {
            localStorage.setItem('email', userData.email);
        }
        if (userData.id) {
            localStorage.setItem('userId', userData.id.toString());
        }
        console.log('JWT Token stored for user:', username);
    }

    // Clear all auth data
    clearAuthData() {
        localStorage.removeItem('token');
        localStorage.removeItem('username');
        localStorage.removeItem('email');
        localStorage.removeItem('userId');
        console.log('Auth data cleared');
    }

    // Check if user is authenticated (has valid token)
    isAuthenticated() {
        const token = localStorage.getItem('token');
        return !!token;
    }

    // Get JWT token
    getToken() {
        return localStorage.getItem('token');
    }

    // Get current username
    getCurrentUsername() {
        return localStorage.getItem('username');
    }

    // Get current user
    getCurrentUser() {
        return {
            username: localStorage.getItem('username'),
            email: localStorage.getItem('email'),
            id: localStorage.getItem('userId')
        };
    }

    // Login - uses username instead of email
    async login(username, password) {
        try {
            console.log('Attempting login with username:', username);
            const response = await api.post('/auth/login', { 
                username,  // Changed from email to username
                password 
            });
            
            // Backend returns JWT token
            const { token, username: returnedUsername, ...userData } = response.data;
            this.setAuthData(token, returnedUsername, userData);
            console.log('Login successful, JWT token received');
            return response.data;
        } catch (error) {
            console.error('Login failed:', error);
            throw this.handleError(error);
        }
    }

    // Register - still uses email for signup
    async register(userData) {
        try {
            console.log('Attempting registration...');
            const response = await api.post('/auth/register', {
                name: userData.name,
                email: userData.email,
                password: userData.password
            });
            console.log('Registration successful, OTP sent');
            return response.data;
        } catch (error) {
            console.error('Registration failed:', error);
            throw this.handleError(error);
        }
    }

    // Verify OTP
    async verifyOTP(email, otp) {
        try {
            console.log('Verifying OTP...');
            const response = await api.post('/auth/verify-otp', { 
                email, 
                otp 
            });
            
            const { token, username, ...userData } = response.data;
            this.setAuthData(token, username, userData);
            console.log('OTP verified, JWT token received');
            return response.data;
        } catch (error) {
            console.error('OTP verification failed:', error);
            throw this.handleError(error);
        }
    }

    // Resend OTP
    async resendOTP(email) {
        try {
            const response = await api.post('/auth/resend-otp', { email });
            return response.data;
        } catch (error) {
            throw this.handleError(error);
        }
    }

    // Logout
    logout() {
        this.clearAuthData();
        window.location.href = '/';
    }

    // Handle API errors
    handleError(error) {
        if (error.response) {
            return {
                message: error.response.data.message || 'An error occurred',
                status: error.response.status
            };
        } else if (error.request) {
            return {
                message: 'Network error. Please check your connection.',
                status: 503
            };
        } else {
            return {
                message: error.message || 'An unexpected error occurred.',
                status: 500
            };
        }
    }
}

export default new AuthService();