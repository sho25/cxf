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
name|coloc
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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

begin_class
specifier|public
class|class
name|ColocHeaderDocLitTest
extends|extends
name|AbstractHeaderServiceDocLitTest
block|{
specifier|static
specifier|final
name|String
name|TRANSPORT_URI
init|=
literal|"http://localhost:9111/headers"
decl_stmt|;
specifier|private
name|Log
name|logger
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|ColocHeaderDocLitTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|Log
name|getLogger
parameter_list|()
block|{
return|return
name|logger
return|;
block|}
specifier|protected
name|Object
name|getServiceImpl
parameter_list|()
block|{
name|HttpServiceImpl
name|impl
init|=
operator|new
name|HttpServiceImpl
argument_list|()
decl_stmt|;
name|impl
operator|.
name|init
argument_list|(
name|getLogger
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|impl
return|;
block|}
specifier|protected
name|String
name|getTransportURI
parameter_list|()
block|{
return|return
name|TRANSPORT_URI
return|;
block|}
annotation|@
name|javax
operator|.
name|jws
operator|.
name|WebService
argument_list|(
name|portName
operator|=
literal|"SoapPort9000"
argument_list|,
name|serviceName
operator|=
literal|"SOAPHeaderService"
argument_list|,
name|targetNamespace
operator|=
literal|"http://apache.org/headers/doc_lit"
argument_list|,
name|endpointInterface
operator|=
literal|"org.apache.headers.doc_lit.HeaderTester"
argument_list|)
class|class
name|HttpServiceImpl
extends|extends
name|BaseHeaderTesterDocLitImpl
block|{ }
annotation|@
name|Test
specifier|public
name|void
name|dummy
parameter_list|()
block|{     }
block|}
end_class

end_unit

