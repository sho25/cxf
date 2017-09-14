begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|tools
operator|.
name|wadlto
operator|.
name|jaxrs
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|HttpURLConnection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|util
operator|.
name|Base64Utility
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|SecureConnectionHelper
block|{
specifier|private
name|SecureConnectionHelper
parameter_list|()
block|{              }
specifier|public
specifier|static
name|InputStream
name|getStreamFromSecureConnection
parameter_list|(
name|URL
name|url
parameter_list|,
name|String
name|authorizationValue
parameter_list|)
throws|throws
name|IOException
block|{
name|HttpURLConnection
name|conn
init|=
operator|(
name|HttpURLConnection
operator|)
name|url
operator|.
name|openConnection
argument_list|()
decl_stmt|;
name|conn
operator|.
name|setRequestMethod
argument_list|(
literal|"GET"
argument_list|)
expr_stmt|;
name|String
name|encodedAuth
init|=
literal|"Basic "
operator|+
name|Base64Utility
operator|.
name|encode
argument_list|(
name|authorizationValue
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|conn
operator|.
name|setRequestProperty
argument_list|(
literal|"Authorization"
argument_list|,
name|encodedAuth
argument_list|)
expr_stmt|;
return|return
name|conn
operator|.
name|getInputStream
argument_list|()
return|;
block|}
block|}
end_class

end_unit

