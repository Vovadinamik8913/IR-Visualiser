import React, {useState, useEffect, useRef} from 'react';
import Header from './components/Header';
import SVGpart from './components/SVGpart';
import TXTpart from "./components/TXTpart";
import './App.css';
import { generateSvg, optimize, saveByFile, saveByPath} from './api/build-api'
import { getFunctions, getSvgByFunction, getSvgByLine } from './api/svg-api';
import { getCode, getLineNumberFromBlock } from './api/code-api';
import { getSvgWithLoops, getLoopInfo, getNestedLoops } from './api/loops-api';
import { getScevInfo, getScevLoopInfo } from './api/scev-api';
import { getDomTree, getDomTreeChildren } from './api/domtree-api';
import { getMemInfo, getMemOfAccess} from "./api/mem-api";

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
    const [info, setInfo] = useState('');
    const [compilerFlags, setCompilerFlags] =  useState(''); 
    const [generatingFlags, setGeneratingFlags] =  useState('');
    const [selectedBlock, setSelectedBlock] = useState('');

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
    }, [irId]);

    useEffect(() => {
        optionRef.current = selectedOption;
    }, [selectedOption]);

    const handleFileUpload = (file) => {
        if (file.name.endsWith('.ll')) { 
            const reader = new FileReader();
            reader.onload = (event) => {
                setLlContent(event.target.result);
                setSvgContent('');
                setListOfFunctions([]);
            };
            reader.readAsText(file);
        } else {
            alert('Пожалуйста, загрузите файл с расширением .ll');
        }
    };

    const handleBuildByFileRequest = async (folder, file) => {
        try {
            const doneRes = await saveByFile(folder, file);
            if (!doneRes) {
                alert('Произошла ошибка при отправке файла');
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

    const handleBuildByPathRequest = async (folder, path) => {
        try {
            const doneRes = await saveByPath(folder, path);
            if (!doneRes) {
                alert('Произошла ошибка при отправке файла');
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

    const handleGetLoopsInfo = async (funcName) => {
        try {
            const svgBlockNums = await getSvgWithLoops(irId, funcName);
            const nums = svgBlockNums.split('\n')
                .map(s => s.trim())
                .filter(s => s !== '');
            setListOfLoopBlocks(nums);
        } catch (error) {
            console.error('Ошибка запроса:', error);
            alert('Произошла ошибка при выполнении запроса');
        }
    };

    //получение свг
    const handleGetRequest = async (funcName) => {
        if(funcName === ''){
            setSvgContent('');
            return;
        }
        try {
            const svgText = await getSvgByFunction(irIdRef.current, funcName);
            setSvgContent(svgText);
        } catch (error) {
            console.error('Ошибка запроса:', error);
        }
    };

    //клик на блок
    const handleBlockClick = async (blockNumber, blockTitle, howManyClicks) => {
        console.log(selectedOption);
        try {
            const lineToCenter = await getLineNumberFromBlock(irId, blockTitle, selectedFunction);
            setLineNumber(lineToCenter - 1);
            console.log(lineToCenter, 'line');
        } catch (error) {
            console.error("Ошибка при получении номера строки:", error);
        }
        if (selectedOption === "LoopsInfo" && selectedFunction) {
            try {
                const info = await getLoopInfo(irId, selectedFunction, "%" + blockNumber);
                setLoopInfo(info);
            } catch (error) {
                console.error("Ошибка при получении информации о цикле:", error);
                setLoopInfo("Ошибка загрузки данных");
            }
            try {
                const svgText = await getNestedLoops(irId, selectedFunction, "%" + blockNumber, howManyClicks);
                const nums = svgText.split('\n')
                    .map(s => s.trim())
                    .filter(s => s !== '');
                setListOfCurLoop(nums);
                console.log(nums, howManyClicks);
            } catch (error) {
                console.error('Ошибка запроса:', error);
            }
        } else if (selectedOption === "Scev" && selectedFunction) {
            try {
                const info = await getScevLoopInfo(irId, selectedFunction, blockNumber);
                setLoopInfo(info);
            } catch (error) {
                console.error("Ошибка при получении информации о цикле:", error);
                setLoopInfo("Ошибка загрузки данных");
            }
        } else if (selectedOption === "DomTree" && selectedFunction) {
            try {
                const info = await getDomTreeChildren(irId, selectedFunction, "%" + blockNumber);
                setLoopInfo(info);
            } catch (error) {
                console.error("Ошибка при получении информации о дереве:", error);
                setLoopInfo("Ошибка загрузки данных");
            }
        }
    };

    //клик на строку
    const handleLineClick = async (index) => {
        try {

            const info = await getSvgByLine(irIdRef.current, index);
            if (!info || info.length === 0) {
                console.error("Ошибка: getSvgByLine не вернул данные.");
                return;
            }
    
            const functionName = info[0]; 
            setSelectedFunction(functionName);

            const blockLabel = info[2];
            setSelectedBlock(blockLabel);

            await handleGetRequest(functionName);

            console.log(optionRef.current);
            if (optionRef.current === "Scev") {
                try {
                    const scev = await getScevInfo(irIdRef.current, parseInt(index));
                    setInfo(scev);
                    console.log(irIdRef.current, 'update');
                } catch (error) {
                    console.error('Ошибка запроса getScevInfo:', error);
                    alert('Ошибка при получении Scev информации');
                }
            }
            if (optionRef.current === "Memoryssa") {
                try{
                    const mem = await getMemInfo(irIdRef.current, parseInt(index));
                    setInfo(mem);
                } catch (error) {
                    console.error('Ошибка запроса getMemInfo:');
                    alert('Ошибка при получении Memoryssa информации');
                }
            }
        } catch (error) {
            console.error('Ошибка запроса getSvgByLine:', error);
            setSvgContent('');
        }
    };
    
    const handleOptimize= async () => {
        try {
         const newId = await optimize(irId, compilerFlags);
         const id = parseInt(newId, 10);
         if (isNaN(id)) {
             throw new Error('Сервер вернул некорректный ID');
         }
         console.log(irIdRef.current, 'norm');
         setIrId(id);
         if (id) {
             await generateSvg(id);
             const content = await getCode(id);
             setLlContent(content);
             setSvgContent('');
             const svgsNames = await getFunctions(id);
             setListOfFunctions(svgsNames);
         } else {
             alert('Ошибка: не удалось получить имена функций');
         }
        } catch (error) {
          console.error('Ошибка при обработке кода:', error);
        }
    };

    const handleDomTreeSelect = async(blockLabel) => {
        try {
            setSelectedBlock(blockLabel);
        } catch (error) {
            console.error(error);
        }
    }

    const handleGetBlockTitle =async (blockTitle) => {
        const lineToCenter = await getLineNumberFromBlock(irId, blockTitle, selectedFunction);
        setLineNumber(lineToCenter - 1);
    }

    const handleSelect = async(id, flags) => {
        try {
            setIrId(id);
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

    const handleMemoryssa = async (access) => {
        try {
            const line = await getMemOfAccess(irIdRef.current, selectedFunction, access);
            setLineNumber(line);
            console.log(line);
        } catch (error) {
            console.error(error);
        }
    }

    const handleGetDomTree = async() => {
        try {
            return await getDomTree(irIdRef.current, selectedFunction);
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
                onTreeSelect={handleSelect}
                onDomTreeSelect={handleDomTreeSelect}
                onDomTreeLoad={handleGetDomTree}
            />
            <div className="main-container">
                <TXTpart
                    content={llContent}
                    onLineClick={handleLineClick}
                    handleProcessCode={handleOptimize}
                    compilerFlags={compilerFlags}
                    setCompilerFlags={setCompilerFlags}
                    optionRef={optionRef}
                    infoContent={info}
                    line={lineNumber}
                    onMemoryssa={handleMemoryssa}
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
                    selectedBlock={selectedBlock}
                    onDomTree={handleGetBlockTitle}
                />
            </div>
        </div>
    );
}

export default App;
