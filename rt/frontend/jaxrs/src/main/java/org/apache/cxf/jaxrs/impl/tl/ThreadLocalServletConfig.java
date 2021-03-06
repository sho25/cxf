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
name|jaxrs
operator|.
name|impl
operator|.
name|tl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletConfig
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

begin_class
specifier|public
class|class
name|ThreadLocalServletConfig
extends|extends
name|AbstractThreadLocalProxy
argument_list|<
name|ServletConfig
argument_list|>
implements|implements
name|ServletConfig
block|{
specifier|public
name|String
name|getInitParameter
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|get
argument_list|()
operator|.
name|getInitParameter
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
name|Enumeration
argument_list|<
name|String
argument_list|>
name|getInitParameterNames
parameter_list|()
block|{
return|return
name|get
argument_list|()
operator|.
name|getInitParameterNames
argument_list|()
return|;
block|}
specifier|public
name|ServletContext
name|getServletContext
parameter_list|()
block|{
return|return
name|get
argument_list|()
operator|.
name|getServletContext
argument_list|()
return|;
block|}
specifier|public
name|String
name|getServletName
parameter_list|()
block|{
return|return
name|get
argument_list|()
operator|.
name|getServletName
argument_list|()
return|;
block|}
block|}
end_class

end_unit

