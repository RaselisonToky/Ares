import React from 'react';
import ReactDOM from 'react-dom';
import LoginForm from '../../../components/Login';

const LoginPage = () => {
return (
<div>
    <h1>Bienvenue sur la page de connexion</h1>
    <LoginForm />
</div>
);
};

ReactDOM.render(<LoginPage />, document.getElementById('root'));
