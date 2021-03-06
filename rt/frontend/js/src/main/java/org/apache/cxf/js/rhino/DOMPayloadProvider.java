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
name|js
operator|.
name|rhino
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMSource
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
name|Provider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mozilla
operator|.
name|javascript
operator|.
name|Scriptable
import|;
end_import

begin_class
specifier|public
class|class
name|DOMPayloadProvider
extends|extends
name|AbstractDOMProvider
implements|implements
name|Provider
argument_list|<
name|DOMSource
argument_list|>
block|{
specifier|public
name|DOMPayloadProvider
parameter_list|(
name|Scriptable
name|scope
parameter_list|,
name|Scriptable
name|wspVar
parameter_list|,
name|String
name|epAddr
parameter_list|,
name|boolean
name|isBaseAddr
parameter_list|,
name|boolean
name|e4x
parameter_list|)
block|{
name|super
argument_list|(
name|scope
argument_list|,
name|wspVar
argument_list|,
name|epAddr
argument_list|,
name|isBaseAddr
argument_list|,
name|e4x
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

