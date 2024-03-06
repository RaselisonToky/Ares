import React, { useState } from 'react';
import axios from 'axios';

const LoginForm = () => {
    const [username , setUsername] = useState();
    const [password , setPassword] = useState();

    const handleSubmit = async () => {
        try {
            const response = await axios.post(`${'$'}{process.env.REACT_APP_API}/api/v1/auth/login`, {
                username,
                password
            });
            const token = response.data;
            localStorage.setItem('token', token);
        } catch (error) {
            console.error('Error:', error.response.data);
        }
    };

    return (
        <div>
            <h2>Connexion</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label htmlFor="emailOrUsername">Email ou Nom d'utilisateur</label>
                    <input
                        type="text"
                        id="emailOrUsername"
                        name="emailOrUsername"
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label htmlFor="password">Mot de passe</label>
                    <input
                        type="password"
                        id="password"
                        name="password"
                        onChange={(e)=>setPassword(e.target.value)}
                        required
                    />
                </div>
                <button type="submit">Se connecter</button>
            </form>
        </div>
    );
};
export default LoginForm;