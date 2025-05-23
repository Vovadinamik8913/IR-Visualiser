import React, {useEffect, useRef, useState} from 'react';
import Editor from '@monaco-editor/react';
import {handleMount} from './MonacoMount'
import '../styles/TXT.css';

const TXTpart = ({
  content,
  compilerFlags,
  setCompilerFlags,
  generatingFlags,
  handleProcessCode,
  onLineClick, 
  optionRef,
  infoContent,
  line,
  onMemoryssa
}) => {

    const [popupVisible, setPopupVisible] = useState(false);
    const [popupPosition, setPopupPosition] = useState({ x: 0, y: 0 });
    const visibleRef = useRef(popupVisible);
    const posRef = useRef(popupPosition);
    const editorRef = useRef(null);
    const monacoRef = useRef(null);

    useEffect(() => {
        visibleRef.current = popupVisible;
        posRef.current = popupPosition;
    }, [popupVisible, popupPosition]);

    useEffect(() => {
        if (editorRef.current && line) {
            editorRef.current.revealLineInCenter(line);
            const decorations = editorRef.current.deltaDecorations([], [
                {
                    range: new monacoRef.current.Range(line, 1, line, 1),
                    options: {
                        isWholeLine: true,
                        className: 'highlighted-line',
                    }
                }
            ]);

            const timeout = setTimeout(() => {
                editorRef.current.deltaDecorations(decorations, []);
            }, 1000);

            return () => clearTimeout(timeout);
        }
    }, [line]);

    const handleEditorMount = (editor, monaco) => {
        editorRef.current = editor;
        monacoRef.current = monaco;
        editor.onMouseDown((mouseEvent) => {
            if (!mouseEvent.target) return;
            const { lineNumber } = mouseEvent.target.position;
            if (mouseEvent.event.ctrlKey) {
                onLineClick(lineNumber);
                if (optionRef.current === "Scev" || optionRef.current === "Memoryssa") {
                    console.log("Попап должен появиться на строке:", lineNumber);
                    setPopupPosition({
                        x: mouseEvent.event.posx,
                        y: mouseEvent.event.posy
                    });
                    setPopupVisible(true);
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

    const renderMemoryssaContent = () => {
        const content = infoContent?.toString() || "";
        const lines = content.split('\n');

        return lines.map((line, lineIdx) => {
            // Обработка MemoryPhi
            if (line.includes("MemoryPhi")) {
                const blocks = [...line.matchAll(/\{(.*?)\}/g)];
                const result = [];
                let lastIndex = 0;

                blocks.forEach((match, i) => {
                    const full = match[0]; // {entry,1}
                    const inner = match[1]; // entry,1
                    const start = match.index;

                    // Текст до {
                    if (start > lastIndex) {
                        result.push(<span key={`text-${lineIdx}-${i}`}>{line.slice(lastIndex, start)}</span>);
                    }

                    const [label, value] = inner.split(',');

                    result.push(
                        <span key={`block-${lineIdx}-${i}`}>
            {'{'}
                            {label},
            <span
                onClick={() => onMemoryssa(parseInt(value))}
                style={{ cursor: 'pointer', color: 'blueviolet', textDecoration: 'underline' }}
            >
              {value}
            </span>
                            {'}'}
          </span>
                    );

                    lastIndex = start + full.length;
                });

                if (lastIndex < line.length) {
                    result.push(<span key={`end-${lineIdx}`}>{line.slice(lastIndex)}</span>);
                }

                return <div key={`line-${lineIdx}`}>{result}</div>;
            }

            // Обработка MemoryUse / MemoryDef
            const match = line.match(/\((.*?)\)/);
            if (match) {
                const num = match[1];
                const start = match.index;
                const end = start + match[0].length;

                return (
                    <div key={`line-${lineIdx}`}>
                        <span>{line.slice(0, start + 1)}</span>
                        <span
                            onClick={() => onMemoryssa(parseInt(num))}
                            style={{ cursor: 'pointer', color: 'blueviolet', textDecoration: 'underline' }}
                        >
            {num}
          </span>
                        <span>{line.slice(end - 1)}</span>
                    </div>
                );
            }

            // Просто текстовая строка без обработок
            return <div key={`line-${lineIdx}`}>{line}</div>;
        });
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
                    <div className="popup-info"
                        style={{ top: popupPosition.y, left: popupPosition.x }}>
                        {infoContent || "Нет данных"}
                    </div>
                )}
                {popupVisible && optionRef.current === "Memoryssa" && (
                    <div className="popup-info"
                         style={{ top: popupPosition.y, left: popupPosition.x }}>
                        <p>{renderMemoryssaContent()}</p>

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