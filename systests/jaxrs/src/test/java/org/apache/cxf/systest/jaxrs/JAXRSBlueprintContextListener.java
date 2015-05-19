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
name|jaxrs
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|aries
operator|.
name|blueprint
operator|.
name|container
operator|.
name|SimpleNamespaceHandlerSet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|aries
operator|.
name|blueprint
operator|.
name|parser
operator|.
name|NamespaceHandlerSet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|aries
operator|.
name|blueprint
operator|.
name|web
operator|.
name|BlueprintContextListener
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
name|internal
operator|.
name|CXFAPINamespaceHandler
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
name|jaxrs
operator|.
name|blueprint
operator|.
name|JAXRSBPNamespaceHandler
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
name|blueprint
operator|.
name|JAXWSBPNamespaceHandler
import|;
end_import

begin_class
specifier|public
class|class
name|JAXRSBlueprintContextListener
extends|extends
name|BlueprintContextListener
block|{
annotation|@
name|Override
specifier|protected
name|NamespaceHandlerSet
name|getNamespaceHandlerSet
parameter_list|(
name|ServletContext
name|sc
parameter_list|,
name|ClassLoader
name|tccl
parameter_list|)
block|{
name|SimpleNamespaceHandlerSet
name|set
init|=
operator|new
name|SimpleNamespaceHandlerSet
argument_list|()
decl_stmt|;
name|set
operator|.
name|addNamespace
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://cxf.apache.org/blueprint/core"
argument_list|)
argument_list|,
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/schemas/blueprint/core.xsd"
argument_list|)
argument_list|,
operator|new
name|CXFAPINamespaceHandler
argument_list|()
argument_list|)
expr_stmt|;
name|set
operator|.
name|addNamespace
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://cxf.apache.org/blueprint/jaxrs"
argument_list|)
argument_list|,
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/schemas/blueprint/jaxrs.xsd"
argument_list|)
argument_list|,
operator|new
name|JAXRSBPNamespaceHandler
argument_list|()
argument_list|)
expr_stmt|;
name|set
operator|.
name|addNamespace
argument_list|(
name|URI
operator|.
name|create
argument_list|(
literal|"http://cxf.apache.org/blueprint/jaxws"
argument_list|)
argument_list|,
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/schemas/blueprint/jaxws.xsd"
argument_list|)
argument_list|,
operator|new
name|JAXWSBPNamespaceHandler
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|set
return|;
block|}
block|}
end_class

end_unit

