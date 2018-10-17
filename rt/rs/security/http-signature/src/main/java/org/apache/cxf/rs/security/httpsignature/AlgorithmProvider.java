begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|rs
operator|.
name|security
operator|.
name|httpsignature
package|;
end_package

begin_interface
annotation|@
name|FunctionalInterface
specifier|public
interface|interface
name|AlgorithmProvider
block|{
comment|/**      *      * @param keyId      * @return the algorithm name (which is never {@code null})      */
name|String
name|getAlgorithmName
parameter_list|(
name|String
name|keyId
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

