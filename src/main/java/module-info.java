open module substmodel {
    requires transitive beast.pkgmgmt;
    requires transitive beast.base;
    requires transitive beast.fx;
    requires transitive javafx.controls;


    exports substmodels.nucleotide;
    exports substmodels.app.beauti;

    provides beast.base.core.BEASTInterface with
        substmodels.nucleotide.F81,
        substmodels.nucleotide.GTR,
        substmodels.nucleotide.HKY,
        substmodels.nucleotide.JC,
        substmodels.nucleotide.K80,
        substmodels.nucleotide.SYM,
        substmodels.nucleotide.TIM1,
        substmodels.nucleotide.TIM1uf,
        substmodels.nucleotide.TIM2,
        substmodels.nucleotide.TIM2uf,
        substmodels.nucleotide.TIM3,
        substmodels.nucleotide.TIM3uf,
        substmodels.nucleotide.TPM1,
        substmodels.nucleotide.TPM1uf,
        substmodels.nucleotide.TPM2,
        substmodels.nucleotide.TPM2uf,
        substmodels.nucleotide.TPM3,
        substmodels.nucleotide.TPM3uf,
        substmodels.nucleotide.TrN,
        substmodels.nucleotide.TrNef,
        substmodels.nucleotide.TVM,
        substmodels.nucleotide.TVMef;
}
