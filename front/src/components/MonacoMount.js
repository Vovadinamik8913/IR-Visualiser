import '@monaco-editor/react';
import { handleLLVMIRMount } from './modes/llvm-ir-mode';


export const handleMount = (monaco) => {
    handleLLVMIRMount(monaco);
};