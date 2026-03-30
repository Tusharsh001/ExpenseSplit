// src/App.jsx
import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';

// Landing Page
import LandingPage from './components/landing/LandingPage';

// Auth Components
import Login from './components/auth/Login';
import Signup from './components/auth/Signup';
import OTPVerification from './components/auth/OTPVerification';

// Dashboard Components
import Dashboard from './components/Dashboard/Dashboard';
import GroupDetail from './components/Dashboard/GroupDetail';

// Services
import authService from './services/auth';

// Styles
import './styles/dashboard.css';
import './styles/auth.css';
import './styles/landing.css';

// Protected Route Component - Only accessible with valid JWT token
const ProtectedRoute = ({ children }) => {
    if (!authService.isAuthenticated()) {
        return <Navigate to="/login" replace />;
    }
    return children;
};

// Public Route Component - Redirects to dashboard if already logged in
const PublicRoute = ({ children }) => {
    if (authService.isAuthenticated()) {
        return <Navigate to="/dashboard" replace />;
    }
    return children;
};

function App() {
    return (
        <Router>
            <div className="app">
                <Routes>
                    {/* 🏠 PUBLIC LANDING PAGE - First thing users see */}
                    <Route path="/" element={<LandingPage />} />
                    
                    {/* 🔐 PUBLIC AUTH ROUTES - Redirect to dashboard if already logged in */}
                    <Route path="/login" element={
                        <PublicRoute>
                            <Login />
                        </PublicRoute>
                    } />
                    
                    <Route path="/signup" element={
                        <PublicRoute>
                            <Signup />
                        </PublicRoute>
                    } />
                    
                    <Route path="/verify-otp" element={
                        <PublicRoute>
                            <OTPVerification />
                        </PublicRoute>
                    } />
                    
                    {/* 📊 PROTECTED ROUTES - Require valid JWT token */}
                    <Route path="/dashboard" element={
                        <ProtectedRoute>
                            <Dashboard />
                        </ProtectedRoute>
                    } />
                    
                    <Route path="/groups/:groupId" element={
                        <ProtectedRoute>
                            <GroupDetail />
                        </ProtectedRoute>
                    } />
                    
                    {/* 🔄 REDIRECTS */}
                    <Route path="*" element={<Navigate to="/" replace />} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;