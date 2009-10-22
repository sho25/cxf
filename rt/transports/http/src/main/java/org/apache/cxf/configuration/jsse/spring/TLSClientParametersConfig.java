begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|configuration
operator|.
name|jsse
operator|.
name|spring
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
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|GeneralSecurityException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|Unmarshaller
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
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
name|PackageUtils
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
name|configuration
operator|.
name|jsse
operator|.
name|TLSClientParameters
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
name|configuration
operator|.
name|security
operator|.
name|TLSClientParametersType
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
name|staxutils
operator|.
name|StaxUtils
import|;
end_import

begin_comment
comment|/**  * This class provides the TLSClientParameters that programmatically  * configure a HTTPConduit. It is initialized with the JAXB  * type TLSClientParametersType that was used in the Spring configuration  * of the http-conduit bean.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|TLSClientParametersConfig
block|{
specifier|static
name|JAXBContext
name|context
decl_stmt|;
specifier|private
name|TLSClientParametersConfig
parameter_list|()
block|{
comment|//not constructed
block|}
specifier|private
specifier|static
specifier|synchronized
name|JAXBContext
name|getContext
parameter_list|()
throws|throws
name|JAXBException
block|{
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
name|context
operator|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|PackageUtils
operator|.
name|getPackageName
argument_list|(
name|TLSClientParametersType
operator|.
name|class
argument_list|)
argument_list|,
name|TLSClientParametersConfig
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|context
return|;
block|}
specifier|static
name|TLSClientParameters
name|createTLSClientParametersFromType
parameter_list|(
name|TLSClientParametersType
name|params
parameter_list|)
throws|throws
name|GeneralSecurityException
throws|,
name|IOException
block|{
name|TLSClientParameters
name|ret
init|=
operator|new
name|TLSClientParameters
argument_list|()
decl_stmt|;
if|if
condition|(
name|params
operator|.
name|isDisableCNCheck
argument_list|()
condition|)
block|{
name|ret
operator|.
name|setDisableCNCheck
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|params
operator|.
name|isSetCipherSuitesFilter
argument_list|()
condition|)
block|{
name|ret
operator|.
name|setCipherSuitesFilter
argument_list|(
name|params
operator|.
name|getCipherSuitesFilter
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|params
operator|.
name|isSetCipherSuites
argument_list|()
condition|)
block|{
name|ret
operator|.
name|setCipherSuites
argument_list|(
name|params
operator|.
name|getCipherSuites
argument_list|()
operator|.
name|getCipherSuite
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|params
operator|.
name|isSetJsseProvider
argument_list|()
condition|)
block|{
name|ret
operator|.
name|setJsseProvider
argument_list|(
name|params
operator|.
name|getJsseProvider
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|params
operator|.
name|isSetSecureRandomParameters
argument_list|()
condition|)
block|{
name|ret
operator|.
name|setSecureRandom
argument_list|(
name|TLSParameterJaxBUtils
operator|.
name|getSecureRandom
argument_list|(
name|params
operator|.
name|getSecureRandomParameters
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|params
operator|.
name|isSetKeyManagers
argument_list|()
condition|)
block|{
name|ret
operator|.
name|setKeyManagers
argument_list|(
name|TLSParameterJaxBUtils
operator|.
name|getKeyManagers
argument_list|(
name|params
operator|.
name|getKeyManagers
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|params
operator|.
name|isSetTrustManagers
argument_list|()
condition|)
block|{
name|ret
operator|.
name|setTrustManagers
argument_list|(
name|TLSParameterJaxBUtils
operator|.
name|getTrustManagers
argument_list|(
name|params
operator|.
name|getTrustManagers
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|params
operator|.
name|isSetCertConstraints
argument_list|()
condition|)
block|{
name|ret
operator|.
name|setCertConstraints
argument_list|(
name|params
operator|.
name|getCertConstraints
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
specifier|public
specifier|static
name|Object
name|createTLSClientParameters
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|StringReader
name|reader
init|=
operator|new
name|StringReader
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|data
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|reader
argument_list|)
decl_stmt|;
name|Unmarshaller
name|u
decl_stmt|;
try|try
block|{
name|u
operator|=
name|getContext
argument_list|()
operator|.
name|createUnmarshaller
argument_list|()
expr_stmt|;
name|Object
name|obj
init|=
name|u
operator|.
name|unmarshal
argument_list|(
name|data
argument_list|,
name|TLSClientParametersType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|JAXBElement
argument_list|<
name|?
argument_list|>
condition|)
block|{
name|JAXBElement
argument_list|<
name|?
argument_list|>
name|el
init|=
operator|(
name|JAXBElement
argument_list|<
name|?
argument_list|>
operator|)
name|obj
decl_stmt|;
name|obj
operator|=
name|el
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
name|TLSClientParametersType
name|cpt
init|=
operator|(
name|TLSClientParametersType
operator|)
name|obj
decl_stmt|;
return|return
name|createTLSClientParametersFromType
argument_list|(
name|cpt
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

