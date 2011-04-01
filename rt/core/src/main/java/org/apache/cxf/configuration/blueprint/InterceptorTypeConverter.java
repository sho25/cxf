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
name|configuration
operator|.
name|blueprint
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|interceptor
operator|.
name|Interceptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|container
operator|.
name|Converter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|container
operator|.
name|ReifiedType
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|InterceptorTypeConverter
implements|implements
name|Converter
block|{
comment|/** {@inheritDoc}*/
specifier|public
name|boolean
name|canConvert
parameter_list|(
name|Object
name|sourceObject
parameter_list|,
name|ReifiedType
name|targetType
parameter_list|)
block|{
return|return
name|sourceObject
operator|instanceof
name|Interceptor
operator|&&
name|targetType
operator|.
name|getRawClass
argument_list|()
operator|.
name|isInstance
argument_list|(
name|sourceObject
argument_list|)
return|;
block|}
comment|/** {@inheritDoc}*/
specifier|public
name|Object
name|convert
parameter_list|(
name|Object
name|sourceObject
parameter_list|,
name|ReifiedType
name|targetType
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|sourceObject
return|;
block|}
block|}
end_class

end_unit

