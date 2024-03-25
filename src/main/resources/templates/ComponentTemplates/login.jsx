import React, { useState } from 'react';
import axios from 'axios';
import styles from "../../page.module.css";

const LoginForm = () => {
    const [username , setUsername] = useState();
    const [password , setPassword] = useState();

    const handleSubmit = async () => {
        try {
            const response = await axios.post(`${'$'}{process.env.REACT_APP_API}/api/v1/auth/login`, {
                username,
                password
            });
            const token = response.data.token;
            localStorage.setItem('token', token);
            window.location = '/list_marque';
        } catch (error) {
            console.error('Error:', error);
        }
    };

    return (
        <div className={styles.container}>
            <h2 className={styles.h2title} >Connexion</h2>
            <div className={styles.champ}>
                <label htmlFor="emailOrUsername" className={styles.label}>Email ou Nom d'utilisateur</label>
                <input
                    type="text"
                    id="emailOrUsername"
                    name="emailOrUsername"
                    onChange={(e) => setUsername(e.target.value)}
                    className={styles.input}
                    required
                />
            </div>
            <div className={styles.champ}>
                <label htmlFor="password" className={styles.label}>Mot de passe</label>
                <input
                    type="password"
                    id="password"
                    name="password"
                    onChange={(e)=>setPassword(e.target.value)}
                    className={styles.input}
                    required
                />
            </div>
            <button onClick={handleSubmit} className={styles.button}>Se connecter</button>
        </div>
    );
};
export default LoginForm;
