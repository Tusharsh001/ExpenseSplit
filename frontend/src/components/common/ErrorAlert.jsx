// src/components/common/ErrorAlert.jsx
import React from 'react';
import '../../styles/dashboard.css';

const ErrorAlert = ({ message, onRetry }) => {
    return (
        <div className="error-alert">
            <span className="error-icon">⚠️</span>
            <p className="error-message">{message}</p>
            {onRetry && (
                <button className="retry-button" onClick={onRetry}>
                    Try again
                </button>
            )}
        </div>
    );
};

export default ErrorAlert;