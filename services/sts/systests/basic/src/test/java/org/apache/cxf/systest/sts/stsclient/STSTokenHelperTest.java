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
name|systest
operator|.
name|sts
operator|.
name|stsclient
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
name|security
operator|.
name|KeyManagementException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyStore
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyStoreException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|NoSuchAlgorithmException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|UnrecoverableKeyException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|CertificateException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|HttpsURLConnection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|KeyManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|KeyManagerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|TrustManager
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|TrustManagerFactory
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
name|Bus
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
name|BusFactory
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
name|endpoint
operator|.
name|Endpoint
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
name|endpoint
operator|.
name|EndpointException
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
name|endpoint
operator|.
name|EndpointImpl
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
name|interceptor
operator|.
name|LoggingInInterceptor
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
name|interceptor
operator|.
name|LoggingOutInterceptor
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
name|message
operator|.
name|Exchange
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
name|message
operator|.
name|ExchangeImpl
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
name|message
operator|.
name|Message
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
name|message
operator|.
name|MessageImpl
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
name|service
operator|.
name|Service
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
name|service
operator|.
name|ServiceImpl
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
name|service
operator|.
name|model
operator|.
name|BindingInfo
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
name|service
operator|.
name|model
operator|.
name|EndpointInfo
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
name|service
operator|.
name|model
operator|.
name|ServiceInfo
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
name|systest
operator|.
name|sts
operator|.
name|common
operator|.
name|SecurityTestUtil
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
name|systest
operator|.
name|sts
operator|.
name|deployment
operator|.
name|STSServer
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
name|testutil
operator|.
name|common
operator|.
name|AbstractBusClientServerTestBase
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
name|transport
operator|.
name|http
operator|.
name|HTTPConduit
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
name|ws
operator|.
name|security
operator|.
name|SecurityConstants
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
name|ws
operator|.
name|security
operator|.
name|policy
operator|.
name|interceptors
operator|.
name|STSTokenHelper
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
name|ws
operator|.
name|security
operator|.
name|tokenstore
operator|.
name|SecurityToken
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
name|ws
operator|.
name|security
operator|.
name|trust
operator|.
name|STSClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * Some tests for STSClient configuration.  */
end_comment

begin_class
specifier|public
class|class
name|STSTokenHelperTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|static
specifier|final
name|String
name|STSPORT
init|=
name|allocatePort
argument_list|(
name|STSServer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|String
name|STSPORT2
init|=
name|allocatePort
argument_list|(
name|STSServer
operator|.
name|class
argument_list|,
literal|2
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|STS_SERVICE_NAME
init|=
literal|"{http://docs.oasis-open.org/ws-sx/ws-trust/200512/}SecurityTokenService"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TOKEN_TYPE_SAML_2_0
init|=
literal|"http://docs.oasis-open.org/wss/oasis-wss-saml-token-profile-1.1#SAMLV2.0"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SERVICE_ENDPOINT_ASSYMETRIC
init|=
literal|"http://localhost:8081/doubleit/services/doubleitasymmetric"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|STS_X509_WSDL_LOCATION_RELATIVE
init|=
literal|"/SecurityTokenService/X509?wsdl"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|STS_X509_ENDPOINT_NAME
init|=
literal|"{http://docs.oasis-open.org/ws-sx/ws-trust/200512/}X509_Port"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|KEY_TYPE_X509
init|=
literal|"http://docs.oasis-open.org/ws-sx/ws-trust/200512/PublicKey"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SERVICE_ENDPOINT_TRANSPORT
init|=
literal|"https://localhost:8081/doubleit/services/doubleittransportsaml1"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|STS_TRANSPORT_WSDL_LOCATION_RELATIVE
init|=
literal|"/SecurityTokenService/Transport?wsdl"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|STS_TRANSPORT_ENDPOINT_NAME
init|=
literal|"{http://docs.oasis-open.org/ws-sx/ws-trust/200512/}Transport_Port"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CLIENTSTORE
init|=
literal|"/clientstore.jks"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|KEYSTORE_PASS
init|=
literal|"cspass"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|KEY_PASS
init|=
literal|"ckpass"
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|startServers
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
literal|"Server failed to launch"
argument_list|,
comment|// run the server in the same process
comment|// set this to false to fork
name|launchServer
argument_list|(
name|STSServer
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|cleanup
parameter_list|()
throws|throws
name|Exception
block|{
name|SecurityTestUtil
operator|.
name|cleanup
argument_list|()
expr_stmt|;
name|stopAllServers
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSTSAsymmetricBinding
parameter_list|()
throws|throws
name|Exception
block|{
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|()
decl_stmt|;
name|STSClient
name|stsClient
init|=
name|initStsClientAsymmeticBinding
argument_list|(
name|bus
argument_list|)
decl_stmt|;
name|MessageImpl
name|message
init|=
name|prepareMessage
argument_list|(
name|bus
argument_list|,
name|stsClient
argument_list|,
name|SERVICE_ENDPOINT_ASSYMETRIC
argument_list|)
decl_stmt|;
name|STSTokenHelper
operator|.
name|TokenRequestParams
name|params
init|=
operator|new
name|STSTokenHelper
operator|.
name|TokenRequestParams
argument_list|()
decl_stmt|;
name|SecurityToken
name|token
init|=
name|STSTokenHelper
operator|.
name|getToken
argument_list|(
name|message
argument_list|,
name|params
argument_list|)
decl_stmt|;
name|validateSecurityToken
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSTSTransportBinding
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Setup HttpsURLConnection to get STS WSDL
name|configureDefaultHttpsConnection
argument_list|()
expr_stmt|;
name|Bus
name|bus
init|=
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|()
decl_stmt|;
name|STSClient
name|stsClient
init|=
name|initStsClientTransportBinding
argument_list|(
name|bus
argument_list|)
decl_stmt|;
name|TLSClientParameters
name|tlsParams
init|=
name|prepareTLSParams
argument_list|()
decl_stmt|;
operator|(
operator|(
name|HTTPConduit
operator|)
name|stsClient
operator|.
name|getClient
argument_list|()
operator|.
name|getConduit
argument_list|()
operator|)
operator|.
name|setTlsClientParameters
argument_list|(
name|tlsParams
argument_list|)
expr_stmt|;
name|MessageImpl
name|message
init|=
name|prepareMessage
argument_list|(
name|bus
argument_list|,
name|stsClient
argument_list|,
name|SERVICE_ENDPOINT_TRANSPORT
argument_list|)
decl_stmt|;
name|STSTokenHelper
operator|.
name|TokenRequestParams
name|params
init|=
operator|new
name|STSTokenHelper
operator|.
name|TokenRequestParams
argument_list|()
decl_stmt|;
name|SecurityToken
name|token
init|=
name|STSTokenHelper
operator|.
name|getToken
argument_list|(
name|message
argument_list|,
name|params
argument_list|)
decl_stmt|;
name|validateSecurityToken
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
specifier|private
name|STSClient
name|initStsClientAsymmeticBinding
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|bus
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|bus
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|bus
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|STSClient
name|stsClient
init|=
operator|new
name|STSClient
argument_list|(
name|bus
argument_list|)
decl_stmt|;
name|stsClient
operator|.
name|setWsdlLocation
argument_list|(
literal|"http://localhost:"
operator|+
name|STSPORT2
operator|+
name|STS_X509_WSDL_LOCATION_RELATIVE
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setServiceName
argument_list|(
name|STS_SERVICE_NAME
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setEndpointName
argument_list|(
name|STS_X509_ENDPOINT_NAME
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setTokenType
argument_list|(
name|TOKEN_TYPE_SAML_2_0
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setKeyType
argument_list|(
name|KEY_TYPE_X509
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setAllowRenewingAfterExpiry
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setEnableLifetime
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|USERNAME
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|CALLBACK_HANDLER
argument_list|,
literal|"org.apache.cxf.systest.sts.common.CommonCallbackHandler"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|ENCRYPT_USERNAME
argument_list|,
literal|"mystskey"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|ENCRYPT_PROPERTIES
argument_list|,
literal|"clientKeystore.properties"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|SIGNATURE_PROPERTIES
argument_list|,
literal|"clientKeystore.properties"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|STS_TOKEN_USERNAME
argument_list|,
literal|"mystskey"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|STS_TOKEN_PROPERTIES
argument_list|,
literal|"clientKeystore.properties"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|STS_TOKEN_USE_CERT_FOR_KEYINFO
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|IS_BSP_COMPLIANT
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setProperties
argument_list|(
name|props
argument_list|)
expr_stmt|;
return|return
name|stsClient
return|;
block|}
specifier|private
name|STSClient
name|initStsClientTransportBinding
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|bus
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|bus
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|bus
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|STSClient
name|stsClient
init|=
operator|new
name|STSClient
argument_list|(
name|bus
argument_list|)
decl_stmt|;
name|stsClient
operator|.
name|setWsdlLocation
argument_list|(
literal|"https://localhost:"
operator|+
name|STSPORT
operator|+
name|STS_TRANSPORT_WSDL_LOCATION_RELATIVE
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setServiceName
argument_list|(
name|STS_SERVICE_NAME
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setEndpointName
argument_list|(
name|STS_TRANSPORT_ENDPOINT_NAME
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setTokenType
argument_list|(
name|TOKEN_TYPE_SAML_2_0
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setAllowRenewingAfterExpiry
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setEnableLifetime
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|USERNAME
argument_list|,
literal|"alice"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|CALLBACK_HANDLER
argument_list|,
literal|"org.apache.cxf.systest.sts.common.CommonCallbackHandler"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|STS_TOKEN_USERNAME
argument_list|,
literal|"mystskey"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|STS_TOKEN_PROPERTIES
argument_list|,
literal|"clientKeystore.properties"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|STS_TOKEN_USE_CERT_FOR_KEYINFO
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|IS_BSP_COMPLIANT
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
name|stsClient
operator|.
name|setProperties
argument_list|(
name|props
argument_list|)
expr_stmt|;
return|return
name|stsClient
return|;
block|}
specifier|private
name|MessageImpl
name|prepareMessage
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|STSClient
name|stsClient
parameter_list|,
name|String
name|serviceAddress
parameter_list|)
throws|throws
name|EndpointException
block|{
name|MessageImpl
name|message
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|STS_CLIENT
argument_list|,
name|stsClient
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|Message
operator|.
name|ENDPOINT_ADDRESS
argument_list|,
name|serviceAddress
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|ServiceInfo
name|si
init|=
operator|new
name|ServiceInfo
argument_list|()
decl_stmt|;
name|Service
name|s
init|=
operator|new
name|ServiceImpl
argument_list|(
name|si
argument_list|)
decl_stmt|;
name|EndpointInfo
name|ei
init|=
operator|new
name|EndpointInfo
argument_list|()
decl_stmt|;
name|Endpoint
name|ep
init|=
operator|new
name|EndpointImpl
argument_list|(
name|bus
argument_list|,
name|s
argument_list|,
name|ei
argument_list|)
decl_stmt|;
name|ei
operator|.
name|setBinding
argument_list|(
operator|new
name|BindingInfo
argument_list|(
name|si
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|setExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|put
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|,
name|ep
argument_list|)
expr_stmt|;
return|return
name|message
return|;
block|}
specifier|private
name|void
name|configureDefaultHttpsConnection
parameter_list|()
throws|throws
name|NoSuchAlgorithmException
throws|,
name|KeyStoreException
throws|,
name|CertificateException
throws|,
name|IOException
throws|,
name|KeyManagementException
block|{
comment|// For localhost testing only
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|HttpsURLConnection
operator|.
name|setDefaultHostnameVerifier
argument_list|(
operator|new
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|HostnameVerifier
argument_list|()
block|{
specifier|public
name|boolean
name|verify
parameter_list|(
name|String
name|hostname
parameter_list|,
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|SSLSession
name|sslSession
parameter_list|)
block|{
return|return
literal|"localhost"
operator|.
name|equals
argument_list|(
name|hostname
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|TrustManagerFactory
name|trustManagerFactory
init|=
name|TrustManagerFactory
operator|.
name|getInstance
argument_list|(
name|TrustManagerFactory
operator|.
name|getDefaultAlgorithm
argument_list|()
argument_list|)
decl_stmt|;
name|KeyStore
name|keyStore
init|=
name|loadClientKeystore
argument_list|()
decl_stmt|;
name|trustManagerFactory
operator|.
name|init
argument_list|(
name|keyStore
argument_list|)
expr_stmt|;
name|TrustManager
index|[]
name|trustManagers
init|=
name|trustManagerFactory
operator|.
name|getTrustManagers
argument_list|()
decl_stmt|;
name|SSLContext
name|sc
init|=
name|SSLContext
operator|.
name|getInstance
argument_list|(
literal|"SSL"
argument_list|)
decl_stmt|;
name|sc
operator|.
name|init
argument_list|(
literal|null
argument_list|,
name|trustManagers
argument_list|,
operator|new
name|java
operator|.
name|security
operator|.
name|SecureRandom
argument_list|()
argument_list|)
expr_stmt|;
name|HttpsURLConnection
operator|.
name|setDefaultSSLSocketFactory
argument_list|(
name|sc
operator|.
name|getSocketFactory
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|TLSClientParameters
name|prepareTLSParams
parameter_list|()
throws|throws
name|KeyStoreException
throws|,
name|IOException
throws|,
name|NoSuchAlgorithmException
throws|,
name|CertificateException
throws|,
name|UnrecoverableKeyException
block|{
name|TLSClientParameters
name|tlsParams
init|=
operator|new
name|TLSClientParameters
argument_list|()
decl_stmt|;
name|tlsParams
operator|.
name|setDisableCNCheck
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|KeyStore
name|trustStore
init|=
name|loadClientKeystore
argument_list|()
decl_stmt|;
name|TrustManagerFactory
name|trustFactory
init|=
name|TrustManagerFactory
operator|.
name|getInstance
argument_list|(
name|TrustManagerFactory
operator|.
name|getDefaultAlgorithm
argument_list|()
argument_list|)
decl_stmt|;
name|trustFactory
operator|.
name|init
argument_list|(
name|trustStore
argument_list|)
expr_stmt|;
name|TrustManager
index|[]
name|tm
init|=
name|trustFactory
operator|.
name|getTrustManagers
argument_list|()
decl_stmt|;
name|tlsParams
operator|.
name|setTrustManagers
argument_list|(
name|tm
argument_list|)
expr_stmt|;
name|KeyStore
name|keyStore
init|=
name|loadClientKeystore
argument_list|()
decl_stmt|;
name|KeyManagerFactory
name|keyFactory
init|=
name|KeyManagerFactory
operator|.
name|getInstance
argument_list|(
name|KeyManagerFactory
operator|.
name|getDefaultAlgorithm
argument_list|()
argument_list|)
decl_stmt|;
name|keyFactory
operator|.
name|init
argument_list|(
name|keyStore
argument_list|,
name|KEY_PASS
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
name|KeyManager
index|[]
name|km
init|=
name|keyFactory
operator|.
name|getKeyManagers
argument_list|()
decl_stmt|;
name|tlsParams
operator|.
name|setKeyManagers
argument_list|(
name|km
argument_list|)
expr_stmt|;
return|return
name|tlsParams
return|;
block|}
specifier|private
name|KeyStore
name|loadClientKeystore
parameter_list|()
throws|throws
name|KeyStoreException
throws|,
name|IOException
throws|,
name|NoSuchAlgorithmException
throws|,
name|CertificateException
block|{
name|KeyStore
name|keystore
init|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
name|KeyStore
operator|.
name|getDefaultType
argument_list|()
argument_list|)
decl_stmt|;
name|InputStream
name|keystoreStream
init|=
name|STSTokenHelperTest
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
name|CLIENTSTORE
argument_list|)
decl_stmt|;
try|try
block|{
name|keystore
operator|.
name|load
argument_list|(
name|keystoreStream
argument_list|,
name|KEYSTORE_PASS
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|keystoreStream
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
name|keystore
return|;
block|}
specifier|private
name|void
name|validateSecurityToken
parameter_list|(
name|SecurityToken
name|token
parameter_list|)
block|{
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|token
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|TOKEN_TYPE_SAML_2_0
argument_list|,
name|token
operator|.
name|getTokenType
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|token
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|token
operator|.
name|getExpires
argument_list|()
operator|.
name|after
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Assertion"
argument_list|,
name|token
operator|.
name|getToken
argument_list|()
operator|.
name|getLocalName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

