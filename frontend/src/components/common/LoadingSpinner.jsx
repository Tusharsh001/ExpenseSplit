// src/components/common/LoadingSpinner.jsx
import React from 'react';
import '../../styles/dashboard.css';

const LoadingSpinner = ({ size = 'medium', message = 'Loading...' }) => {
    return (
        <div className={`loading-spinner ${size}`}>
            <div className="spinner"></div>
            {message && <p className="spinner-message">{message}</p>}
        </div>
    );
};

export default LoadingSpinner;