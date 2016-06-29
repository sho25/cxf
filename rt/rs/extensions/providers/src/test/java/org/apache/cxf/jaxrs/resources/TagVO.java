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
name|resources
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_class
annotation|@
name|XmlRootElement
specifier|public
class|class
name|TagVO
block|{
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|String
name|group
decl_stmt|;
specifier|private
name|Integer
name|attrInt
decl_stmt|;
specifier|public
name|TagVO
parameter_list|()
block|{               }
specifier|public
name|TagVO
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|group
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|group
operator|=
name|group
expr_stmt|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|n
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|n
expr_stmt|;
block|}
specifier|public
name|void
name|setGroup
parameter_list|(
name|String
name|g
parameter_list|)
block|{
name|this
operator|.
name|group
operator|=
name|g
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|String
name|getGroup
parameter_list|()
block|{
return|return
name|group
return|;
block|}
annotation|@
name|XmlAttribute
specifier|public
name|Integer
name|getAttrInt
parameter_list|()
block|{
return|return
name|attrInt
return|;
block|}
specifier|public
name|void
name|setAttrInt
parameter_list|(
name|Integer
name|attrInt
parameter_list|)
block|{
name|this
operator|.
name|attrInt
operator|=
name|attrInt
expr_stmt|;
block|}
block|}
end_class

end_unit

