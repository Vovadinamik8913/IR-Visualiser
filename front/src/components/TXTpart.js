import React from 'react';
import Editor from '@monaco-editor/react';
import {handleMount} from './MonacoMount'
import './styles/TXT.css';


const TXTpart = ({
  content,
  compilerFlags, setCompilerFlags,
  handleProcessCode,
  onLineClick 
}) => {

    const handleEditorMount = (editor, monaco) => {
        // Подписываемся на событие мыши — нажатие (mouseDown)
        editor.onMouseDown((mouseEvent) => {
          if (mouseEvent.target && mouseEvent.target.position) {
            if (mouseEvent.event.ctrlKey) {  // Проверяем, зажат ли ctrl
              const { lineNumber } = mouseEvent.target.position;
              onLineClick(lineNumber);
            }
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
          ) : (
            <div className="ir-placeholder">
              Загрузите файл с расширением .ll
            </div>
          )}
        </div>
    );
};

export default TXTpart;