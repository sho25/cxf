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
name|javascript
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|frontend
operator|.
name|ServerFactoryBean
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
name|jaxws
operator|.
name|JaxWsProxyFactoryBean
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
name|test
operator|.
name|AbstractCXFSpringTest
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
specifier|abstract
class|class
name|JavascriptRhinoTest
extends|extends
name|AbstractCXFSpringTest
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
literal|"TestPort"
argument_list|)
decl_stmt|;
specifier|protected
name|JavascriptTestUtilities
name|testUtilities
decl_stmt|;
specifier|protected
name|JaxWsProxyFactoryBean
name|clientProxyFactory
decl_stmt|;
specifier|protected
name|ServiceInfo
name|serviceInfo
decl_stmt|;
specifier|protected
name|ServerFactoryBean
name|serverFactoryBean
decl_stmt|;
specifier|protected
name|Object
name|rawImplementor
decl_stmt|;
specifier|private
name|Endpoint
name|endpoint
decl_stmt|;
specifier|public
name|JavascriptRhinoTest
parameter_list|()
throws|throws
name|Exception
block|{
name|super
argument_list|()
expr_stmt|;
name|testUtilities
operator|=
operator|new
name|JavascriptTestUtilities
argument_list|(
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|testUtilities
operator|.
name|addDefaultNamespaces
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|setupRhino
parameter_list|(
name|String
name|serviceEndpointBean
parameter_list|,
name|String
name|testsJavascript
parameter_list|,
name|boolean
name|validation
parameter_list|)
throws|throws
name|Exception
block|{
name|testUtilities
operator|.
name|setBus
argument_list|(
name|getBean
argument_list|(
name|Bus
operator|.
name|class
argument_list|,
literal|"cxf"
argument_list|)
argument_list|)
expr_stmt|;
name|testUtilities
operator|.
name|initializeRhino
argument_list|()
expr_stmt|;
name|serverFactoryBean
operator|=
name|getBean
argument_list|(
name|ServerFactoryBean
operator|.
name|class
argument_list|,
name|serviceEndpointBean
argument_list|)
expr_stmt|;
name|endpoint
operator|=
name|serverFactoryBean
operator|.
name|getServer
argument_list|()
operator|.
name|getEndpoint
argument_list|()
expr_stmt|;
comment|// we need to find the implementor.
name|rawImplementor
operator|=
name|serverFactoryBean
operator|.
name|getServiceBean
argument_list|()
expr_stmt|;
name|testUtilities
operator|.
name|readResourceIntoRhino
argument_list|(
literal|"/org/apache/cxf/javascript/cxf-utils.js"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ServiceInfo
argument_list|>
name|serviceInfos
init|=
name|endpoint
operator|.
name|getService
argument_list|()
operator|.
name|getServiceInfos
argument_list|()
decl_stmt|;
comment|// there can only be one.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|serviceInfos
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|serviceInfo
operator|=
name|serviceInfos
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|testUtilities
operator|.
name|loadJavascriptForService
argument_list|(
name|serviceInfo
argument_list|)
expr_stmt|;
name|testUtilities
operator|.
name|readResourceIntoRhino
argument_list|(
name|testsJavascript
argument_list|)
expr_stmt|;
if|if
condition|(
name|validation
condition|)
block|{
name|endpoint
operator|.
name|getService
argument_list|()
operator|.
name|put
argument_list|(
name|Message
operator|.
name|SCHEMA_VALIDATION_ENABLED
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|String
name|getAddress
parameter_list|()
block|{
return|return
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getAddress
argument_list|()
return|;
block|}
block|}
end_class

end_unit

