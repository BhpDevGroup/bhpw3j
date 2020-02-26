package io.bhpw3j.contract;

import static io.bhpw3j.utils.Strings.isEmpty;

import io.bhpw3j.contract.abi.model.BhpContractEvent;
import io.bhpw3j.contract.abi.model.BhpContractFunction;
import io.bhpw3j.contract.abi.model.BhpContractInterface;
import io.bhpw3j.model.types.ContractParameterType;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Contract {

    private final ScriptHash contractScriptHash;

    private ContractDeploymentScript deploymentScript;

    private BhpContractInterface abi;

    /**
     * Creates a new contract with the set deployment script.
     *
     * @param deploymentScript Deployment script instance.
     * @param abi ABI instance representing the contract's interfaces.
     */
    public Contract(ContractDeploymentScript deploymentScript, BhpContractInterface abi) {
        this.deploymentScript = deploymentScript;
        this.contractScriptHash = deploymentScript.getContractScriptHash();
        this.abi = abi;
    }

    /**
     * Creates a new contract with the given script hash
     *
     * @param contractScriptHash Contract script hash in little-endian order.
     * @deprecated
     */
    @Deprecated
    public Contract(byte[] contractScriptHash) {
        this.contractScriptHash = new ScriptHash(contractScriptHash);
    }

    /**
     * Creates a new contract with the given script hash and ABI.
     *
     * @param contractScriptHash Contract script hash in little-endian order.
     * @param abi ABI instance representing the contract's interfaces.
     * @deprecated
     */
    @Deprecated
    public Contract(byte[] contractScriptHash, BhpContractInterface abi) {
        this(contractScriptHash);
        this.abi = abi;
    }

    /**
     * Creates a new contract with the given script hash
     *
     * @param contractScriptHash Contract script hash.
     */
    public Contract(ScriptHash contractScriptHash) {
        this.contractScriptHash = contractScriptHash;
    }

    /**
     * Creates a new contract with the given script hash and ABI.
     *
     * @param contractScriptHash Contract script hash in little-endian order.
     * @param abi The contract's ABI.
     */
    public Contract(ScriptHash contractScriptHash, BhpContractInterface abi) {
        this(contractScriptHash);
        this.abi = abi;
    }

    /**
     * Get the contract script hash.
     *
     * @return the script hash object instance.
     */
    public ScriptHash getContractScriptHash() {
        return contractScriptHash;
    }

    /**
     * Get the deployment script for this contract.
     *
     * @return the deployment script instance.
     */
    public ContractDeploymentScript getDeploymentScript() {
        return deploymentScript;
    }

    /**
     * Get the ABI interfaces for this contract.
     *
     * @return the ABI object instance.
     */
    public BhpContractInterface getAbi() {
        return abi;
    }

    /**
     * Set the ABI interface for this contract.
     *
     * @param abi The ABI instance to be set.
     * @return this Contract instance.
     */
    public Contract abi(BhpContractInterface abi) {
        this.abi = abi;
        return this;
    }

    /**
     * Get the contract entry point function.
     *
     * @return an {@link Optional} with the contract function representing the entry point.
     */
    public Optional<BhpContractFunction> getEntryPoint() {
        return getFunction(abi.getEntryPoint());
    }

    /**
     * Get the entry point function's parameters.
     *
     * @return a {@link List} with the contract parameters.
     */
    public List<ContractParameter> getEntryPointParameters() {
        return getFunctionParameters(abi.getEntryPoint());
    }

    /**
     * Get the entry point function's return type.
     *
     * @return an {@link Optional} with the return type.
     */
    public Optional<ContractParameterType> getEntryPointReturnType() {
        return getFunctionReturnType(abi.getEntryPoint());
    }

    /**
     * Get all contract functions.
     *
     * @return a {@link List} with all contract functions.
     */
    public List<BhpContractFunction> getFunctions() {
        return abi.getFunctions();
    }

    /**
     * Get all contract events.
     *
     * @return a {@link List} with all contract events.
     */
    public List<BhpContractEvent> getEvents() {
        return abi.getEvents();
    }

    /**
     * Get all function parameters by function name.
     *
     * @param functionName The function name to get all function parameters.
     * @return a {@link List} with all function parameters.
     */
    public List<ContractParameter> getFunctionParameters(final String functionName) {
        throwIfABINotSet();
        return abi.getFunctions()
            .stream()
            .filter(f -> !isEmpty(f.getName()))
            .filter(f -> f.getName().equals(functionName))
            .findFirst()
            .map(BhpContractFunction::getParameters)
            .orElse(Collections.emptyList());
    }

    /**
     * Get the function object representation by function name.
     *
     * @param functionName The function name.
     * @return an {@link Optional} with the contract function.
     */
    public Optional<BhpContractFunction> getFunction(final String functionName) {
        return abi.getFunctions()
            .stream()
            .filter(f -> !isEmpty(f.getName()))
            .filter(f -> f.getName().equals(functionName))
            .findFirst();
    }

    /**
     * Get the function return type by function name.
     *
     * @param functionName The function name.
     * @return an {@link Optional} with the function return type.
     */
    public Optional<ContractParameterType> getFunctionReturnType(final String functionName) {
        throwIfABINotSet();
        return abi.getFunctions()
            .stream()
            .filter(f -> !isEmpty(f.getName()))
            .filter(f -> f.getName().equals(functionName))
            .findFirst()
            .map(BhpContractFunction::getReturnType);
    }

    /**
     * Get the event object representation by event name.
     *
     * @param eventName The event name.
     * @return an {@link Optional} with the contract event.
     */
    public Optional<BhpContractEvent> getEvent(final String eventName) {
        return abi.getEvents()
            .stream()
            .filter(f -> !isEmpty(f.getName()))
            .filter(f -> f.getName().equals(eventName))
            .findFirst();
    }

    /**
     * Get all event parameters by event name.
     *
     * @param eventName The event name to get all event parameters.
     * @return a {@link List} with all event parameters.
     */
    public List<ContractParameter> getEventParameters(final String eventName) {
        throwIfABINotSet();
        return getEvent(eventName)
            .map(BhpContractEvent::getParameters)
            .orElse(Collections.emptyList());
    }

    private void throwIfABINotSet() {
        if (getAbi() == null) {
            throw new IllegalStateException("ABI should be set first.");
        }
    }

}
