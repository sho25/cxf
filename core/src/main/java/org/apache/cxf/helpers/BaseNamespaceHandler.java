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
name|helpers
package|;
end_package

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
name|aries
operator|.
name|blueprint
operator|.
name|NamespaceHandler
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

begin_class
specifier|public
specifier|abstract
class|class
name|BaseNamespaceHandler
implements|implements
name|NamespaceHandler
block|{
specifier|private
name|NamespaceHandler
name|cxfApiNamespaceHandler
init|=
operator|new
name|CXFAPINamespaceHandler
argument_list|()
decl_stmt|;
comment|/**      * If namespace handler's schema imports other schemas from cxf-core bundle, this method      * may be used to delegate to<code>CXFAPINamespaceHandler</code> to resolve imported schema.      * @param namespace      * @return if namespace may be resolved by CXFAPINamespaceHandler valid URL is returned. Otherwise      * returns<code>null</code>      */
specifier|protected
name|URL
name|findCoreSchemaLocation
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
return|return
name|cxfApiNamespaceHandler
operator|.
name|getSchemaLocation
argument_list|(
name|namespace
argument_list|)
return|;
block|}
block|}
end_class

end_unit

