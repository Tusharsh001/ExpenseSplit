// src/services/api.js
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080';

const api = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
    // Add this for debugging
    withCredentials: false // Change to true if your backend needs cookies
});

// Add request logging to see what URLs are being called
api.interceptors.request.use(
    (config) => {
        console.log('🌐 API Request:', config.method.toUpperCase(), config.baseURL + config.url);
        
        const token = localStorage.getItem('token');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
            console.log('🔑 Token added to request');
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

// Response interceptor to handle token errors
api.interceptors.response.use(
    (response) => {
        console.log('✅ API Response:', response.status, response.config.url);
        return response;
    },
    (error) => {
        console.log('❌ API Error:', error.response?.status, error.config?.url);
        
        if (error.response?.status === 401) {
            console.log('🚫 Unauthorized - token expired');
            localStorage.clear();
            window.location.href = '/login';
        }
        return Promise.reject(error);
    }
);

// Group APIs
export const groupAPI = {
    getMyGroups: () => api.get('/groups/my-groups'),
    getGroupById: (groupId) => api.get(`/groups/${groupId}`),
    createGroup: (data) => api.post('/groups', data),
    updateGroup: (groupId, data) => api.put(`/groups/${groupId}`, data),
    deleteGroup: (groupId) => api.delete(`/groups/${groupId}`),
    addMembers: (groupId, memberIds) => api.post(`/groups/${groupId}/members`, memberIds),
    removeMember: (groupId, userId) => api.delete(`/groups/${groupId}/members/${userId}`),
    leaveGroup: (groupId) => api.post(`/groups/${groupId}/leave`),
};

// Expense APIs
export const expenseAPI = {
    createExpense: (data) => api.post('/expenses', data),
    getGroupExpenses: (groupId) => api.get(`/expenses/group/${groupId}`),
    getMyExpenses: () => api.get('/expenses/my-expenses'),
};

export default api;