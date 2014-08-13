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
name|management
operator|.
name|web
operator|.
name|logging
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ext
operator|.
name|Provider
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
name|search
operator|.
name|SearchContextProvider
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
annotation|@
name|Provider
specifier|public
class|class
name|MockApp
extends|extends
name|Application
block|{
specifier|private
specifier|static
specifier|final
name|AtomPullServer
name|LOGS
decl_stmt|;
static|static
block|{
name|LOGS
operator|=
operator|new
name|AtomPullServer
argument_list|()
expr_stmt|;
name|LOGS
operator|.
name|setLogger
argument_list|(
literal|"org.apache.cxf.management.web.logging.Generate"
argument_list|)
expr_stmt|;
name|LOGS
operator|.
name|init
argument_list|()
expr_stmt|;
block|}
specifier|private
specifier|static
specifier|final
name|AtomFeedProvider
name|FEED
init|=
operator|new
name|AtomFeedProvider
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|AtomEntryProvider
name|ENTRY
init|=
operator|new
name|AtomEntryProvider
argument_list|()
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|BootstrapStorage
name|BOOTSTRAP_STORAGE
init|=
operator|new
name|BootstrapStorage
argument_list|(
operator|new
name|SimpleXMLSettingsStorage
argument_list|()
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|BootstrapStorage
operator|.
name|SettingsProvider
name|SETTINGS
init|=
operator|new
name|BootstrapStorage
operator|.
name|SettingsProvider
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|getClasses
parameter_list|()
block|{
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
init|=
operator|new
name|HashSet
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|AtomPullServer
operator|.
name|class
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|AtomFeedProvider
operator|.
name|class
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|AtomEntryProvider
operator|.
name|class
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|BootstrapStorage
operator|.
name|class
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|BootstrapStorage
operator|.
name|SettingsProvider
operator|.
name|class
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|SearchContextProvider
operator|.
name|class
argument_list|)
expr_stmt|;
return|return
name|classes
return|;
block|}
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
name|classes
operator|.
name|add
argument_list|(
name|LOGS
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|FEED
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|ENTRY
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|BOOTSTRAP_STORAGE
argument_list|)
expr_stmt|;
name|classes
operator|.
name|add
argument_list|(
name|SETTINGS
argument_list|)
expr_stmt|;
return|return
name|classes
return|;
block|}
block|}
end_class

end_unit

