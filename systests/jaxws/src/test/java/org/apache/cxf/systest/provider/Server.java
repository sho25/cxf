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
name|provider
package|;
end_package

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
name|xml
operator|.
name|ws
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
name|databinding
operator|.
name|source
operator|.
name|SourceDataBinding
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
name|AbstractBusTestServerBase
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
name|TestUtil
import|;
end_import

begin_class
specifier|public
class|class
name|Server
extends|extends
name|AbstractBusTestServerBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
name|Server
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
name|void
name|run
parameter_list|()
block|{
name|Object
name|implementor
init|=
operator|new
name|HWSourcePayloadProvider
argument_list|()
decl_stmt|;
name|String
name|address
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SOAPServiceProviderRPCLit/SoapPortProviderRPCLit8"
decl_stmt|;
name|Endpoint
name|ep
init|=
name|Endpoint
operator|.
name|create
argument_list|(
name|implementor
argument_list|)
decl_stmt|;
name|ep
operator|.
name|publish
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|SourceDataBinding
operator|.
name|PREFERRED_FORMAT
argument_list|,
literal|"dom"
argument_list|)
expr_stmt|;
name|address
operator|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SOAPServiceProviderRPCLit/SoapPortProviderRPCLit8-dom"
expr_stmt|;
name|ep
operator|=
name|Endpoint
operator|.
name|create
argument_list|(
name|implementor
argument_list|)
expr_stmt|;
name|ep
operator|.
name|setProperties
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|ep
operator|.
name|publish
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|SourceDataBinding
operator|.
name|PREFERRED_FORMAT
argument_list|,
literal|"sax"
argument_list|)
expr_stmt|;
name|address
operator|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SOAPServiceProviderRPCLit/SoapPortProviderRPCLit8-sax"
expr_stmt|;
name|ep
operator|=
name|Endpoint
operator|.
name|create
argument_list|(
name|implementor
argument_list|)
expr_stmt|;
name|ep
operator|.
name|setProperties
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|ep
operator|.
name|publish
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|SourceDataBinding
operator|.
name|PREFERRED_FORMAT
argument_list|,
literal|"stax"
argument_list|)
expr_stmt|;
name|address
operator|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SOAPServiceProviderRPCLit/SoapPortProviderRPCLit8-stax"
expr_stmt|;
name|ep
operator|=
name|Endpoint
operator|.
name|create
argument_list|(
name|implementor
argument_list|)
expr_stmt|;
name|ep
operator|.
name|setProperties
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|ep
operator|.
name|publish
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|SourceDataBinding
operator|.
name|PREFERRED_FORMAT
argument_list|,
literal|"cxf.stax"
argument_list|)
expr_stmt|;
name|address
operator|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SOAPServiceProviderRPCLit/SoapPortProviderRPCLit8-cxfstax"
expr_stmt|;
name|ep
operator|=
name|Endpoint
operator|.
name|create
argument_list|(
name|implementor
argument_list|)
expr_stmt|;
name|ep
operator|.
name|setProperties
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|ep
operator|.
name|publish
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
name|SourceDataBinding
operator|.
name|PREFERRED_FORMAT
argument_list|,
literal|"stream"
argument_list|)
expr_stmt|;
name|address
operator|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SOAPServiceProviderRPCLit/SoapPortProviderRPCLit8-stream"
expr_stmt|;
name|ep
operator|=
name|Endpoint
operator|.
name|create
argument_list|(
name|implementor
argument_list|)
expr_stmt|;
name|ep
operator|.
name|setProperties
argument_list|(
name|map
argument_list|)
expr_stmt|;
name|ep
operator|.
name|publish
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|implementor
operator|=
operator|new
name|HWSoapMessageProvider
argument_list|()
expr_stmt|;
name|address
operator|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SOAPServiceProviderRPCLit/SoapPortProviderRPCLit1"
expr_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|,
name|implementor
argument_list|)
expr_stmt|;
name|implementor
operator|=
operator|new
name|HWDOMSourceMessageProvider
argument_list|()
expr_stmt|;
name|address
operator|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SOAPServiceProviderRPCLit/SoapPortProviderRPCLit2"
expr_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|,
name|implementor
argument_list|)
expr_stmt|;
name|implementor
operator|=
operator|new
name|HWDOMSourcePayloadProvider
argument_list|()
expr_stmt|;
name|address
operator|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SOAPServiceProviderRPCLit/SoapPortProviderRPCLit3"
expr_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|,
name|implementor
argument_list|)
expr_stmt|;
name|implementor
operator|=
operator|new
name|HWSAXSourceMessageProvider
argument_list|()
expr_stmt|;
name|address
operator|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SOAPServiceProviderRPCLit/SoapPortProviderRPCLit4"
expr_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|,
name|implementor
argument_list|)
expr_stmt|;
name|implementor
operator|=
operator|new
name|HWStreamSourceMessageProvider
argument_list|()
expr_stmt|;
name|address
operator|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SOAPServiceProviderRPCLit/SoapPortProviderRPCLit5"
expr_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|,
name|implementor
argument_list|)
expr_stmt|;
name|implementor
operator|=
operator|new
name|HWSAXSourcePayloadProvider
argument_list|()
expr_stmt|;
name|address
operator|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SOAPServiceProviderRPCLit/SoapPortProviderRPCLit6"
expr_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|,
name|implementor
argument_list|)
expr_stmt|;
name|implementor
operator|=
operator|new
name|HWStreamSourcePayloadProvider
argument_list|()
expr_stmt|;
name|address
operator|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SOAPServiceProviderRPCLit/SoapPortProviderRPCLit7"
expr_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|,
name|implementor
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
try|try
block|{
name|Server
name|s
init|=
operator|new
name|Server
argument_list|()
decl_stmt|;
name|s
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"done!"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

