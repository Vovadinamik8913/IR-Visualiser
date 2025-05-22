import React, { useState } from 'react';
import '../styles/Upload.css';
import '../App.css';
import config from "../config/config.js";

const Upload = ({onClose, onFileUpload, onBuildByFileRequest, onBuildByPathRequest}) => {
    const [activeTab, setActiveTab] = useState('fileUpload');
    const [selectedFile, setSelectedFile] = useState(null);
    const [folderName, setFolderName] = useState('');
    const [path, setPath] = useState('');
  
    const handleTabChange = (tab) => {
      setActiveTab(tab);
      setSelectedFile(null);
      setPath('');
      setFolderName('');
    };
    
    const handleFolderChange = (event) => {
        setFolderName(event.target.value);
    };

    const handlePathChange = (event) => {
        setPath(event.target.value);
    };

    const handleFileUpload = async (event) => {
        const file = event.target.files[0];
        if (file && file.name.endsWith('.ll')) { // Проверяем, что файл имеет расширение .ll
            await onFileUpload(file); // Ожидаем завершения запроса
            setSelectedFile(file);
            setFolderName('');
        } else {
            alert("Пожалуйста, загрузите файл с расширением .ll");
        }
    };

    // Обработка нажатия на кнопку Готово
    const handleFileDoneClick = async () => {
        if (folderName && selectedFile) {
            onBuildByFileRequest(folderName, selectedFile);
            onClose();
        } else {
            alert('Введите имя папки и загрузите .ll файл');
        }
    };

    const handlePathDoneClick = async () => {
      if (folderName && path) {
          onBuildByPathRequest(folderName, path);
          onClose();
      } else {
          alert('Введите имя папки и введите путь до .ll файла');
      }
  };


    return (
        <div className="overlay-container">
            <div className="overlay-content">
                <button onClick={onClose} className="close-button">X</button>

                {/* Табы */}
                { config.container === "false" && (
                <div className="tabs">
                  <button
                    className={activeTab === 'fileUpload' ? 'active' : ''}
                    onClick={() => handleTabChange('fileUpload')}
                  >
                    Загрузка через файл
                  </button>
                  <button
                    className={activeTab === 'otherMethod' ? 'active' : ''}
                    onClick={() => handleTabChange('otherMethod')}
                  >
                    Загрузка через путь
                  </button>
                </div>
                )}
                  {/* Содержимое вкладок */}
                  {activeTab === 'fileUpload' && (
                    <div className="tab-panel">
                      <input
                      type="file"
                      id="file-upload"
                      accept=".ll"
                      onChange={handleFileUpload}
                      style={{display: 'none'}}/>

                      <label
                          htmlFor="file-upload"
                          className="upload-button">
                          { selectedFile ? selectedFile.name : 'Загрузите файл'}
                      </label>

                      <input
                          type="text"
                          value={folderName}
                          onChange={handleFolderChange}
                          placeholder="Введите имя папки"
                      />
                  
                      <button
                        onClick={handleFileDoneClick}
                        className="build-button">
                        Готово
                      </button>
                    </div>
                  )}
                  {activeTab === 'otherMethod' && (
                    <div className="tab-panel">
                      <input
                        type="text"
                        value={path}
                        onChange={handlePathChange}
                        placeholder="Введите путь до файла"
                      />

                      <input
                        type="text"
                        value={folderName}
                        onChange={handleFolderChange}
                        placeholder="Введите имя папки"
                      />
                      <button
                        onClick={handlePathDoneClick}
                        className="build-button">
                        Готово
                      </button>
                    </div>
                  )}
            </div>
        </div>
    );
};

export default Upload;