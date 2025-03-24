import React, {useEffect, useRef, useState} from 'react';
import Editor from '@monaco-editor/react';
import {handleMount} from './MonacoMount'
import './styles/TXT.css';

const TXTpart = ({
  content,
  compilerFlags, setCompilerFlags,
  generatingFlags,
  handleProcessCode,
  onLineClick, 
  optionRef,
  infoContent,
  line
}) => {

    const [popupVisible, setPopupVisible] = useState(false);
    const [popupPosition, setPopupPosition] = useState({ x: 0, y: 0 });
    const visibleRef = useRef(popupVisible);
    const posRef = useRef(popupPosition);
    const editorRef = useRef(null);

    useEffect(() => {
        visibleRef.current = popupVisible;
        posRef.current = popupPosition;
    }, [popupVisible, popupPosition]);

    useEffect(() => {
        if (editorRef.current && line) {
            editorRef.current.revealLineInCenter(line);
        }
    }, [line]);

    const handleEditorMount = (editor, monaco) => {
        editorRef.current = editor;
        editor.onMouseDown((mouseEvent) => {
            if (!mouseEvent.target) return;
            const { lineNumber } = mouseEvent.target.position;
            if (mouseEvent.event.ctrlKey) {
                onLineClick(lineNumber);
                if (optionRef.current === "Scev") {
                    console.log("Попап должен появиться на строке:", lineNumber);
                    setPopupPosition({
                        x: mouseEvent.event.posx,
                        y: mouseEvent.event.posy
                    });
                    setPopupVisible(true);
                    console.log(posRef);
            }
          }
        });

        editor.onDidScrollChange(() => {
            setPopupVisible(false);
        });
    };
    
    const handleCompilerFlagsChange = (event) => {
      setCompilerFlags(event.target.value);
    };

    return (
        <div className="window">
          {generatingFlags && <div className="info">
            <label className="text-input">
              Скомпилирован с:
            </label>
            <div className="flex-grow">
              <label
                value={generatingFlags}
                className="text-input"
              />
            </div>
          </div>}
          <div className="info">
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