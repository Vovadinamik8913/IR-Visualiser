import React from 'react';

const Window = ({ title }) => {
    return (
        <div className="window">
            <h2>{title}</h2>
            <p>This is the content of {title}.</p>
        </div>
    );
};

export default Window;