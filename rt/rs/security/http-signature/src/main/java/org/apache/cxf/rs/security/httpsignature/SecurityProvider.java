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

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Provider
import|;
end_import

begin_interface
annotation|@
name|FunctionalInterface
specifier|public
interface|interface
name|SecurityProvider
block|{
comment|/**      *      * @param keyId      * @return the security provider (which is never {@code null})      * @throws NullPointerException if the provided key ID is {@code null}      */
name|Provider
name|getProvider
parameter_list|(
name|String
name|keyId
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

