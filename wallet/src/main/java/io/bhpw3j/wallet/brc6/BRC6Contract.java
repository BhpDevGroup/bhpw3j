package io.bhpw3j.wallet.brc6;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.bhpw3j.model.types.ContractParameterType;

import java.util.List;
import java.util.Objects;

public class BRC6Contract {

    @JsonProperty("script")
    private String script;

    @JsonProperty("parameters")
    private List<BRC6Parameter> BRC6Parameters;

    @JsonProperty("deployed")
    private Boolean deployed;

    public BRC6Contract() {
    }

    public BRC6Contract(String script, List<BRC6Parameter> BRC6Parameters, Boolean deployed) {
        this.script = script;
        this.BRC6Parameters = BRC6Parameters;
        this.deployed = deployed;
    }

    public String getScript() {
        return script;
    }

    public List<BRC6Parameter> getParameters() {
        return BRC6Parameters;
    }

    public Boolean getDeployed() {
        return deployed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BRC6Contract)) return false;
        BRC6Contract contract = (BRC6Contract) o;
        return Objects.equals(getScript(), contract.getScript()) &&
                Objects.equals(getParameters(), contract.getParameters()) &&
                Objects.equals(getDeployed(), contract.getDeployed());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getScript(), getParameters(), getDeployed());
    }

    @Override
    public String toString() {
        return "Contract{" +
                "script='" + script + '\'' +
                ", brc6Parameters=" + BRC6Parameters +
                ", deployed=" + deployed +
                '}';
    }

    public static class BRC6Parameter {

        @JsonProperty("name")
        private String paramName;

        @JsonProperty("type")
        private ContractParameterType paramType;

        public BRC6Parameter(String paramName, ContractParameterType paramType) {
            this.paramName = paramName;
            this.paramType = paramType;
        }

        public BRC6Parameter() {
        }

        public String getParamName() {
            return paramName;
        }

        public ContractParameterType getParamType() {
            return paramType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BRC6Parameter BRC6Parameter = (BRC6Parameter) o;
            return Objects.equals(paramName, BRC6Parameter.paramName) &&
                    paramType == BRC6Parameter.paramType;
        }

        @Override
        public int hashCode() {
            return Objects.hash(paramName, paramType);
        }

        @Override
        public String toString() {
            return "BRC6Parameter{" +
                    "paramName='" + paramName + '\'' +
                    ", paramType=" + paramType +
                    '}';
        }
    }
}
