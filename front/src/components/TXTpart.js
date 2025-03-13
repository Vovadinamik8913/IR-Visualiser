import React, {useState} from 'react';
import Editor from '@monaco-editor/react';
import {handleMount} from './MonacoMount'
import './styles/TXT.css';


const TXTpart = ({
  content,
  compilerFlags, setCompilerFlags,
  handleProcessCode,
  onLineClick, 
  optionRef,
  infoContent
}) => {

    const [popupVisible, setPopupVisible] = useState(false);
    const [popupPosition, setPopupPosition] = useState({ x: 0, y: 0 });
    const handleEditorMount = (editor, monaco) => {
      editor.onMouseDown((mouseEvent) => {
          if (!mouseEvent.target) return;
  
          const { position } = mouseEvent.target;
          if (!position) return;
  
          const { lineNumber } = position;
  
          if (mouseEvent.event.ctrlKey) {  
              onLineClick(lineNumber);
              if (optionRef.current === "Scev") {
                console.log("Попап должен появиться на строке:", lineNumber);
                setPopupVisible(true);
                setPopupPosition({
                    x: mouseEvent.event.clientX + 10,
                    y: mouseEvent.event.clientY + 10
                });
            }
          }
      });

      editor.onMouseDown((mouseEvent) => {
        if (popupVisible && !mouseEvent.target.element?.closest(".popup-info")) {
          setPopupVisible(false);
        }
      });
  };
  
    
    const handleCompilerFlagsChange = (event) => {
      setCompilerFlags(event.target.value);
    };

    return (
        <div className="window">
          <div className="info">
            {/* Ввод ключей для компилятора */}
            <div className="flex-grow">
              <input
                id="compiler-flags"
                type="text"
                value={compilerFlags}
                onChange={handleCompilerFlagsChange}
                className="text-input"
                placeholder="Введите ключи для компилятора"
              />
            </div>
            <div>
              <button className="open-upload-button" onClick={handleProcessCode}>
                Применить</button>
            </div>
          </div>
          {content ? (
            <div className='txt-container'>
              <Editor
                height="100%"
                language={"LLVM IR"}
                value={content}
                options={{
                  fontSize: 16,
                  readOnly: true,
                }}
                beforeMount={handleMount}
                onMount={handleEditorMount}
              />
              {popupVisible && optionRef.current === "Scev" && (
                <div 
                  className="popup-info" 
                  style={{ top: popupPosition.y, left: popupPosition.x }}
                >
                  {infoContent || "Нет данных"}
                </div>
              )}
            </div>
          ) : (
            <div className="ir-placeholder">
              Загрузите файл с расширением .ll
            </div>
          )}
        </div>
    );
};

export default TXTpart;