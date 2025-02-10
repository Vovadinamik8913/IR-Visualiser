import React, {useState} from 'react';
import Upload from './Upload';
import OptTree from './OptTree';
import './styles/Header.css';

const Header = ({onFileUpload, onBuildByFileRequest, onBuildByPathRequest}) => {
    const [isLoading, setIsLoading] = useState(false); 
    const [isOverlayOpen, setIsOverlayOpen] = useState(false);
    const [isDropboxOpen, setIsDropboxOpen] = useState(false);
    const [selectedOption, setSelectedOption] = useState("Анализы");
    const [isOptTreeOpen, setIsOptTreeOpen] = useState(false);

    const handleOpenOverlay = () => {
      setIsOverlayOpen(true);
    };
    
    const handleCloseOverlay = () => {
      setIsOverlayOpen(false);
    };

    const handleSelect = (option) => {
      setSelectedOption(option);
      setIsDropboxOpen(false); // Закрываем меню после выбора
    };    

    return (
        <header className="header">
            <div className="header-left">
              <div className="opt-tree">
                <button onClick={() => setIsOptTreeOpen(true)} className="opt-tree-button">
                  Дерево оптимизаций
                </button>
                <OptTree isOpen={isOptTreeOpen} onClose={() => setIsOptTreeOpen(false)}/>
              </div>
              <div class="dropdown">
                <button onClick={() => setIsDropboxOpen(!isDropboxOpen)} class="dropdown-button">
                  {selectedOption}
                </button>
                {isDropboxOpen && (
                  <div className="dropdown-menu">
                    <div>
                      <div className="dropdown-item" onClick={() => handleSelect("LoopsInfo")}>LoopsInfo</div>
                      <div className="dropdown-item" onClick={() => handleSelect("Опция 2")}>Опция 2</div>
                      <div className="dropdown-item" onClick={() => handleSelect("Опция 3")}>Опция 3</div>
                      <div className="dropdown-item" onClick={() => handleSelect("Опция 4")}>Опция 4</div>
                    </div>
                  </div>
                )}
              </div> 
            </div>

            <div className="header-center">
                <button onClick={handleOpenOverlay} className="open-upload-button">
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
