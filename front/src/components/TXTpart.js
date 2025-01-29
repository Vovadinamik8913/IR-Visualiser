import React, {useState} from 'react';
import Editor from '@monaco-editor/react';
import {handleMount} from './MonacoMount'


const TXTpart = ({content, onLineClick }) => {
    const [lineIndex, setLineIndex] = useState(null);

    const handleEditorMount = (editor, monaco) => {
        // Подписываемся на событие мыши — нажатие (mouseDown)
        editor.onMouseDown((mouseEvent) => {
          if (mouseEvent.target && mouseEvent.target.position) {
            const { lineNumber, column } = mouseEvent.target.position;
            console.log('Clicked line:', lineNumber);
            setLineIndex(lineNumber);
            onLineClick(lineIndex);
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
                  fontSize: 24,
                  readOnly: true,
                }}
                beforeMount={handleMount}
                onMount={handleEditorMount}
              />
          ) : (
              <p
              style={{
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                width: '100%',
                height: '300px',
                border: '2px dashed #ccc',
                color: '#888',
                fontSize: '16px'
              }}> Загрузите файл .ll для отображения содержимого.</p>
          )}
        </div>
    );
};

export default TXTpart;