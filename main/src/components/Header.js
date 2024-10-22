import React from 'react';

const Header = () => {
    const handleFileUpload = (event) => {
        const file = event.target.files[0];
        if (file) {
            console.log('Uploaded file:', file.name);
        }
    };

    return (
        <header className="header">
            <div className="header-content">
                <h1>IR VISUALIZER</h1>
                <input type="file" id="file-upload" onChange={handleFileUpload}/>
                <label htmlFor="file-upload" className="upload-button">Upload File</label>
            </div>
        </header>
    );
};

export default Header;