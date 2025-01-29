import React, {useState} from 'react';
import Upload from './Upload';

const Header = ({onFileUpload, onBuildByFileRequest, onBuildByPathRequest}) => {
    const [isLoading, setIsLoading] = useState(false);
    const [isOverlayOpen, setIsOverlayOpen] = useState(false);

    const handleOpenOverlay = () => {
        setIsOverlayOpen(true);
      };
    
      const handleCloseOverlay = () => {
        setIsOverlayOpen(false);
      };

    return (
        <header className="header">
            <div className="header-center">
                <button onClick={handleOpenOverlay} className="upload-button">
                  Открыть окно загрузки
                </button>
            </div>
            <h1 className="header-title">IR VISUALIZER</h1>
            {isLoading && <div className="loading-bar"></div>}
            {/* Сам оверлей со вкладками. Показывается только, если isOverlayOpen === true */}
            {isOverlayOpen && (
              <Upload
                onClose={handleCloseOverlay}
                onFileUpload={onFileUpload}
                onBuildByFileRequest={onBuildByFileRequest}
                onBuildByPathRequest={onBuildByPathRequest}
              />
            )}        
        </header>
    );
};

export default Header;
