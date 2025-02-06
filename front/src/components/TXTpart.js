import React, {useState} from 'react';
import Editor from '@monaco-editor/react';
import {handleMount} from './MonacoMount'


const TXTpart = ({content, onLineClick }) => {

    const handleEditorMount = (editor, monaco) => {
        // Подписываемся на событие мыши — нажатие (mouseDown)
        editor.onMouseDown((mouseEvent) => {
          if (mouseEvent.target && mouseEvent.target.position) {
            const { lineNumber, column } = mouseEvent.target.position;
            onLineClick(lineNumber);
          }
        });
      };

    return (
        <div className="window">
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