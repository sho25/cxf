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
name|databinding
operator|.
name|source
operator|.
name|mime
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|commons
operator|.
name|schema
operator|.
name|extensions
operator|.
name|ExtensionRegistry
import|;
end_import

begin_class
specifier|public
class|class
name|CustomExtensionRegistry
extends|extends
name|ExtensionRegistry
block|{
specifier|public
name|CustomExtensionRegistry
parameter_list|()
block|{
name|registerSerializer
argument_list|(
name|MimeAttribute
operator|.
name|class
argument_list|,
operator|new
name|MimeSerializer
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

