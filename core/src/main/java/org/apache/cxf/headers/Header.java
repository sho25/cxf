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
name|headers
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|DataBinding
import|;
end_import

begin_class
specifier|public
class|class
name|Header
block|{
specifier|public
enum|enum
name|Direction
block|{
name|DIRECTION_IN
block|,
name|DIRECTION_OUT
block|,
name|DIRECTION_INOUT
block|}
empty_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_LIST
init|=
name|Header
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".list"
decl_stmt|;
specifier|private
name|DataBinding
name|dataBinding
decl_stmt|;
specifier|private
name|QName
name|name
decl_stmt|;
specifier|private
name|Object
name|object
decl_stmt|;
specifier|private
name|Direction
name|direction
init|=
name|Header
operator|.
name|Direction
operator|.
name|DIRECTION_OUT
decl_stmt|;
specifier|public
name|Header
parameter_list|(
name|QName
name|q
parameter_list|,
name|Object
name|o
parameter_list|)
block|{
name|this
argument_list|(
name|q
argument_list|,
name|o
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Header
parameter_list|(
name|QName
name|q
parameter_list|,
name|Object
name|o
parameter_list|,
name|DataBinding
name|b
parameter_list|)
block|{
name|object
operator|=
name|o
expr_stmt|;
name|name
operator|=
name|q
expr_stmt|;
name|dataBinding
operator|=
name|b
expr_stmt|;
block|}
specifier|public
name|DataBinding
name|getDataBinding
parameter_list|()
block|{
return|return
name|dataBinding
return|;
block|}
specifier|public
name|void
name|setDataBinding
parameter_list|(
name|DataBinding
name|dataBinding
parameter_list|)
block|{
name|this
operator|.
name|dataBinding
operator|=
name|dataBinding
expr_stmt|;
block|}
specifier|public
name|QName
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|QName
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|Object
name|getObject
parameter_list|()
block|{
return|return
name|object
return|;
block|}
specifier|public
name|void
name|setObject
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
name|this
operator|.
name|object
operator|=
name|object
expr_stmt|;
block|}
specifier|public
name|void
name|setDirection
parameter_list|(
name|Direction
name|hdrDirection
parameter_list|)
block|{
name|this
operator|.
name|direction
operator|=
name|hdrDirection
expr_stmt|;
block|}
specifier|public
name|Direction
name|getDirection
parameter_list|()
block|{
return|return
name|direction
return|;
block|}
block|}
end_class

end_unit

