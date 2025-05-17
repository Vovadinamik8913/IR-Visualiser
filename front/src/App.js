import React, {useState, useEffect, useRef} from 'react';
import Header from './components/Header';
import SVGpart from './components/SVGpart';
import TXTpart from "./components/TXTpart";
import './App.css';

import {
    generateSvg,
    optimize,
    saveByFile,
    saveByPath
} from './components/api/build-api';

import {
    getFunctions,
    getSvgByFunction,
    getSvgByLine
} from './components/api/svg-api';


import {
    getCode,
    getLineNumberFromBlock
} from './components/api/code-api';

import {
    getSvgWithLoops,
    getLoopInfo,
    getNestedLoops
} from './components/api/loops-api';

import {
    getScevInfo,
    getScevLoopInfo
} from './components/api/scev-api';

function App() {
    const [llContent, setLlContent] = useState(null);
    const [listOfFunctions, setListOfFunctions] = useState([]);
    const [listOfLoopBlocks, setListOfLoopBlocks] = useState([]);
    const [listOfCurLoop, setListOfCurLoop] = useState([]);
    const [svgContent, setSvgContent] = useState('');
    const [irId, setIrId] = useState(0);
    const [lineNumber, setLineNumber] = useState(0);
    const [selectedFunction, setSelectedFunction] = useState('');
    const [selectedOption, setSelectedOption] = useState("Анализы");
    const [loopInfo, setLoopInfo] = useState('');
    const [scevInfo, setScevInfo] = useState('');
    const [compilerFlags, setCompilerFlags] =  useState(''); 
    const [generatingFlags, setGeneratingFlags] =  useState('');
    const [blockNumberToCenter, setBlockNumberToCenter] = useState(null);

    const irIdRef = useRef(irId);
    const optionRef = useRef(selectedOption);

    useEffect(() => {
      document.body.style.overflow = 'hidden';
      return () => {
        document.body.style.overflow = 'auto';
      };
    }, []);

    useEffect(() => {
      irIdRef.current = irId;
      optionRef.current = selectedOption;
    }, [irId, selectedOption]);

    /*Функция для загрузки .ll файла*/
    const handleFileUpload = (file) => {
        if (!file.name.endsWith('.ll')) {
            alert('Пожалуйста, загрузите файл с расширением .ll');
        }

        const reader = new FileReader();
        reader.onload = (event) => {
            setLlContent(event.target.result); // Сохраняем текстовое содержимое .ll файла
            setSvgContent('');
            setListOfFunctions([]);
        };
        reader.readAsText(file);
    };

    /*Запрос на получения имен функций и генерацию свг изображений через файл*/
    const handleBuildByFileRequest = async (folder, file) => {
        try {
            const doneRes = await saveByFile(folder, file);
            if (!doneRes) {
                alert('Произошла ошибка при отправке файла на сервер');
            } else {
                const id = parseInt(doneRes, 10);
                if (isNaN(id)) {
                    throw new Error('Сервер вернул некорректный ID');
                }
                setIrId(id);
                if (id) {
                    await generateSvg(id);
                    const svgsNames = await getFunctions(id);
                    setListOfFunctions(svgsNames);
                } else {
                    alert('Ошибка: не удалось получить имена функций');
                }
            }
        } catch (error) {
            console.error('Ошибка запроса:', error);
            alert('Произошла ошибка при выполнении запроса "/build/file"');
        }
    };

    /*Запрос на получения имен функций и генерацию свг изображений через путь*/
    const handleBuildByPathRequest = async (folder, path) => {
        try {
            const doneRes = await saveByPath(folder, path);
            if (!doneRes) {
                alert('Произошла ошибка при отправке пути на сервер');
            } else {
                const id = parseInt(doneRes, 10);
                console.log(id);
                if (isNaN(id)) {
                    throw new Error('Сервер вернул некорректный ID');
                }
                setIrId(id);
                if (id) {
                    await generateSvg(id);
                    const content = await getCode(id);
                    setLlContent(content);
                    const svgsNames = await getFunctions(id);
                    setListOfFunctions(svgsNames);
                } else {
                    alert('Ошибка: не удалось получить имена функций');
                }
            }
        } catch (error) {
            console.error('Ошибка запроса:', error);
            alert('Произошла ошибка при выполнении запроса "/build/file"');
        }
    };

    /*Запрос на получение информации о блоках с циклами*/
    const handleGetLoopsInfo = async (funcName) => {
        try {
            const svgBlockNums = await getSvgWithLoops(irId, funcName);
            const nums = svgBlockNums.split('\n')
                .map(s => s.trim())
                .filter(s => s !== '')
                .map(Number);
            setListOfLoopBlocks(nums);
        } catch (error) {
            console.error('Ошибка запроса:', error);
            alert('Произошла ошибка при выполнении запроса');
        }
    };

    /*Получение cfg по имени функции*/
    const handleGetRequest = async (funcName) => {
        try {
            const svgText = await getSvgByFunction(irId, funcName);
            setSvgContent(svgText);
        } catch (error) {
            console.error('Ошибка запроса:', error);
            alert('Произошла ошибка при выполнении запроса');
        }
    };

    /*Обработка кликов на блоки в cfg*/
    const handleBlockClick = async (blockNumber, blockTitle, howManyClicks) => {
        console.log(selectedOption);
        /*Получение номера строки*/
        try {
            const lineToCenter = await getLineNumberFromBlock(irId, blockTitle, selectedFunction);
            setLineNumber(lineToCenter);
        } catch (error) {
            console.error("Ошибка при получении номера строки:", error);
        }

        /*Получение информации о вложенных циклах (LOOPS)*/
        if (selectedOption === "LoopsInfo" && selectedFunction) {
            //получение информации о цикле
            try {
                const info = await getLoopInfo(irId, selectedFunction, blockNumber);
                setLoopInfo(info);
            } catch (error) {
                console.error("Ошибка при получении информации о цикле:", error);
                setLoopInfo("Ошибка загрузки данных");
            }
            //получение информации о вложенности циклов
            try {
                const svgText = await getNestedLoops(irId, selectedFunction, blockNumber, howManyClicks);
                const nums = svgText.split('\n')
                    .map(s => s.trim())
                    .filter(s => s !== '')
                    .map(Number);
                setListOfCurLoop(nums);
                console.log(nums, howManyClicks);
            } catch (error) {
                console.error('Ошибка при запросе на получение информации о вложенных циклах:', error);
            }
        /*Получение информации для scalar evolution для циклов*/
        } else if (selectedOption === "Scev" && selectedFunction) {
            try {
                const info = await getScevLoopInfo(irId, selectedFunction, blockNumber);
                setLoopInfo(info);
            } catch (error) {
                console.error("Ошибка при получении SCEV информации о цикле:", error);
                setLoopInfo("Ошибка загрузки данных");
            }
        }
    };

    /*Обработка кликов на строку*/
    const handleLineClick = async (index) => {
        //получение cfg по клику на строку
        try {
            const info = await getSvgByLine(irIdRef.current, index);
            if (!info || info.length === 0) {
                console.error("Ошибка при запросе получение SVG для строки");
                return;
            }
            const functionName = info[0]; 
            setSelectedFunction(functionName);

            //!!!!!!!!!!!РАЗОБРАТЬСЯ!!!!!!!!!!!
            const selectedBlock = info[info.length - 1].replace('%', '');
            setBlockNumberToCenter(selectedBlock);
            console.log(blockNumberToCenter);
            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

            //запрос на получение свг
            try {
                const svgText = await getSvgByFunction(irIdRef.current, functionName);
                setSvgContent(svgText);
            } catch (error) {
                console.error('Ошибка при загрузке SVG:', error);
                alert('Ошибка при загрузке SVG');
            }
        } catch (error) {
            console.error('Ошибка запроса getSvgByLine:', error);
            alert('Произошла ошибка при обработке строки');
        }

        //получение SCEV для строки
        console.log(optionRef.current);
        if (optionRef.current === "Scev") {
            try {
                const scev = await getScevInfo(irIdRef.current, parseInt(index));
                setScevInfo(scev);
            } catch (error) {
                console.error('Ошибка запроса getScevInfo:', error);
                alert('Ошибка при получении Scev информации');
            }
        }
    };

    /*Получение оптимизированного кода*/
    const handleOptimize= async () => {
        try {
         const newId = await optimize(irId, compilerFlags);
         const id = parseInt(newId, 10);
         if (isNaN(id)) {
             throw new Error('Сервер вернул некорректный ID');
         }
         setIrId(id);
         if (id) {
             await generateSvg(id);
             const content = await getCode(id);
             setLlContent(content);
             const svgsNames = await getFunctions(id);
             setListOfFunctions(svgsNames);
         } else {
             alert('Ошибка: не удалось получить имена функций');
         }
        } catch (error) {
          console.error('Ошибка при обработке кода:', error);
        }
    };

    /*Выбор IR из дерева оптимизаций*/
    const handleSelect = async(id, flags) => {
        try {
            const content = await getCode(id);
            setLlContent(content);
            const svgsNames = await getFunctions(id);
            setListOfFunctions(svgsNames);
            setGeneratingFlags(flags);
            setSvgContent('');
            setSelectedFunction('');
            setCompilerFlags('');
        } catch (error) {
            console.error('Ошибка при обработке кода:', error);
        }
    }

    return (
        <div className="App">
            <Header 
                onFileUpload={handleFileUpload}
                onBuildByFileRequest={handleBuildByFileRequest}
                onBuildByPathRequest={handleBuildByPathRequest}
                onAnalysChange={setSelectedOption}
                onSelect={handleSelect}
            />

            <div className="main-container">
                <TXTpart
                    content={llContent}
                    onLineClick={handleLineClick}
                    handleProcessCode={handleOptimize}
                    compilerFlags={compilerFlags}
                    setCompilerFlags={setCompilerFlags}
                    optionRef={optionRef}
                    infoContent={scevInfo}
                    line={lineNumber}
                />

                <SVGpart
                    functions={listOfFunctions}
                    selectedFunction={selectedFunction}
                    setSelectedFunction={setSelectedFunction}
                    llContent={llContent}
                    svgContent={svgContent}
                    onGetRequest={handleGetRequest}
                    selectedOption={selectedOption}
                    infoContent={loopInfo}
                    onBlockClick={handleBlockClick}
                    listOfLoopBlocks={listOfLoopBlocks}
                    listOfCurLoop={listOfCurLoop}
                    onGetLoopsInfo={handleGetLoopsInfo}
                    selectedBlock={blockNumberToCenter}
                />

            </div>
        </div>
    );
}

export default App;
