import React, {useState} from 'react';
import Editor from '@monaco-editor/react';
import {handleMount} from './MonacoMount'


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
        <div className="window form">
          <div className="info">
            {/* Ввод ключей для компилятора */}
            <div className="flex-grow">
              <input
                id="compiler-flags"
                type="text"
                value={compilerFlags}
                onChange={handleCompilerFlagsChange}
                className="text-input"
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
            <div className="ir-placeholder" style={{
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
              width: '100%',
              height: '300px',
              border: '2px dashed #ccc',
              color: '#888',
              fontSize: '16px'
          }}>
              Загрузите файл с расширением .ll
          </div>
          )}
        </div>
    );
};

export default TXTpart;