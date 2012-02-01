begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|logbrowser
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Application
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
name|ext
operator|.
name|provider
operator|.
name|atom
operator|.
name|AtomEntryProvider
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
name|ext
operator|.
name|provider
operator|.
name|atom
operator|.
name|AtomFeedProvider
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
name|management
operator|.
name|web
operator|.
name|browser
operator|.
name|bootstrapping
operator|.
name|BootstrapStorage
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
name|management
operator|.
name|web
operator|.
name|browser
operator|.
name|bootstrapping
operator|.
name|SimpleXMLSettingsStorage
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
name|management
operator|.
name|web
operator|.
name|logging
operator|.
name|atom
operator|.
name|AtomPullServer
import|;
end_import

begin_class
specifier|public
class|class
name|App
extends|extends
name|Application
block|{
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|Object
argument_list|>
name|getSingletons
parameter_list|()
block|{
name|Set
argument_list|<
name|Object
argument_list|>
name|classes
init|=
operator|new
name|HashSet
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
comment|// The log browser
name|classes
operator|.
name|add
argument_list|(
operator|new
name|BootstrapStorage
argument_list|(
operator|new
name|SimpleXMLSettingsStorage
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
operator|new
name|BootstrapStorage
operator|.
name|StaticFileProvider
argument_list|()
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
operator|new
name|BootstrapStorage
operator|.
name|SettingsProvider
argument_list|()
argument_list|)
expr_stmt|;
comment|// The pull server
name|AtomPullServer
name|aps
init|=
operator|new
name|AtomPullServer
argument_list|()
decl_stmt|;
name|aps
operator|.
name|setLoggers
argument_list|(
literal|"demo.service:DEBUG,org.apache.cxf.interceptor:INFO"
argument_list|)
expr_stmt|;
name|aps
operator|.
name|init
argument_list|()
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|aps
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
operator|new
name|AtomFeedProvider
argument_list|()
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
operator|new
name|AtomEntryProvider
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|classes
return|;
block|}
block|}
end_class

end_unit

