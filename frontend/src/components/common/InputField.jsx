// src/components/common/InputField.jsx
import React, { useState } from 'react';

const InputField = ({ 
    type = 'text', 
    name,
    placeholder, 
    value, 
    onChange, 
    error,
    icon,
    required = true,
    autoFocus = false,
    disabled = false
}) => {
    const [showPassword, setShowPassword] = useState(false);
    const [isFocused, setIsFocused] = useState(false);
    
    const isPassword = type === 'password';
    const inputType = isPassword ? (showPassword ? 'text' : 'password') : type;

    return (
        <div className="input-wrapper">
            <div className={`input-container ${error ? 'error' : ''} ${isFocused ? 'focused' : ''} ${disabled ? 'disabled' : ''}`}>
                {icon && <span className="input-icon">{icon}</span>}
                
                <input
                    type={inputType}
                    name={name}
                    id={name}
                    placeholder={placeholder}
                    value={value}
                    onChange={onChange}
                    required={required}
                    autoFocus={autoFocus}
                    disabled={disabled}
                    onFocus={() => setIsFocused(true)}
                    onBlur={() => setIsFocused(false)}
                    className="auth-input"
                />
                
                {isPassword && (
                    <button
                        type="button"
                        className="password-toggle"
                        onClick={() => setShowPassword(!showPassword)}
                        tabIndex="-1"
                        disabled={disabled}
                    >
                        {showPassword ? '👁️' : '👁️‍🗨️'}
                    </button>
                )}
            </div>
            {error && <span className="error-message">{error}</span>}
        </div>
    );
};

export default InputField;