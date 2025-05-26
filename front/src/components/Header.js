import React, {useState} from 'react';
import Upload from './Upload';
import OptTree from './OptTree';
import DomTree from './DomTree';
import '../styles/Header.css';

const Header = ({
  onFileUpload,
  onBuildByFileRequest, 
  onBuildByPathRequest, 
  onAnalysChange,
  onTreeSelect,
  onDomTreeSelect,
  onDomTreeLoad,
  onBranchDelete
}) => {
    const [isOverlayOpen, setIsOverlayOpen] = useState(false);
    const [isDropboxOpen, setIsDropboxOpen] = useState(false);
    const [selectedOption, setSelectedOption] = useState("Анализы");
    const [isOptTreeOpen, setIsOptTreeOpen] = useState(false);
    const [isDomTreeOpen, setIsDomTreeOpen] = useState(false);

    const handleOpenOverlay = () => {
      setIsOverlayOpen(true);
    };
    
    const handleCloseOverlay = () => {
      setIsOverlayOpen(false);
    };

    const handleSelect = (option) => {
      setSelectedOption(option);
      setIsDropboxOpen(false);
      onAnalysChange(option);
    };    

    return (
        <header className="header">
            <div className="header-left">
              <div className="opt-tree">
                <button onClick={() => setIsOptTreeOpen(true)} className="opt-tree-button">
                  Дерево оптимизаций
                </button>
                <OptTree
                 isOpen={isOptTreeOpen} 
                 onClose={() => setIsOptTreeOpen(false)}
                 onSelect={onTreeSelect}
                 onBranchDelete={onBranchDelete}
                 />
              </div>
              <div className="dropdown">
                <button onClick={() => setIsDropboxOpen(!isDropboxOpen)} className="dropdown-button">
                  {selectedOption}
                </button>
                {isDropboxOpen && (
                  <div className="dropdown-menu">
                    <div>
                      <div className="dropdown-item" onClick={() => handleSelect("Normal")}>Normal</div>
                      <div className="dropdown-item" onClick={() => handleSelect("LoopsInfo")}>LoopsInfo</div>
                      <div className="dropdown-item" onClick={() => handleSelect("Scev")}>Scev</div>
                      <div className="dropdown-item" onClick={() => handleSelect("Memoryssa")}>Memoryssa</div>
                        <div className='dropdown-item'
                             onClick={() => {
                                 setIsDomTreeOpen(true);
                                 handleSelect("DomTree")}}>
                            DomTree
                        </div>
                    </div>
                  </div>
                )}
              </div> 
            </div>

            <DomTree
                isOpen={isDomTreeOpen}
                onClose={() => setIsDomTreeOpen(false)}
                onSelect={onDomTreeSelect}
                onLoad={onDomTreeLoad}
            />

            <div className="header-center">
                <button onClick={handleOpenOverlay} className="open-upload-button">
                  Открыть окно загрузки
                </button>
            </div>
            
            <h1 className="header-title">IR VISUALIZER</h1>

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
