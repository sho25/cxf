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
name|jaxb
operator|.
name|model
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
name|XmlAccessType
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
name|XmlAccessorType
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
name|XmlElement
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
name|XmlType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|builder
operator|.
name|EqualsBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|builder
operator|.
name|ToStringBuilder
import|;
end_import

begin_comment
comment|/**  * @author shade  */
end_comment

begin_class
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|"widget"
argument_list|,
name|namespace
operator|=
literal|"http://cxf.org.apache/model"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"widget"
argument_list|,
name|namespace
operator|=
literal|"http://cxf.org.apache/model"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
specifier|public
specifier|abstract
class|class
name|Widget
block|{
annotation|@
name|XmlAttribute
argument_list|(
name|required
operator|=
literal|true
argument_list|)
specifier|private
name|long
name|id
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|true
argument_list|,
name|namespace
operator|=
literal|"http://cxf.org.apache/model"
argument_list|)
specifier|private
name|String
name|name
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|false
argument_list|,
name|namespace
operator|=
literal|"http://cxf.org.apache/model"
argument_list|)
specifier|private
name|String
name|serialNumber
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|required
operator|=
literal|true
argument_list|,
name|namespace
operator|=
literal|"http://cxf.org.apache/model"
argument_list|)
specifier|private
name|boolean
name|broken
decl_stmt|;
comment|/**      *       */
specifier|public
name|Widget
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
comment|/**      * @param id      * @param name      * @param serialNumber      * @param broken      */
specifier|public
name|Widget
parameter_list|(
name|long
name|id
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|serialNumber
parameter_list|,
name|boolean
name|broken
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|serialNumber
operator|=
name|serialNumber
expr_stmt|;
name|this
operator|.
name|broken
operator|=
name|broken
expr_stmt|;
block|}
comment|/**      * @return the id      */
specifier|public
name|long
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
comment|/**      * @param id the id to set      */
specifier|public
name|void
name|setId
parameter_list|(
name|long
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
comment|/**      * @return the name      */
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
comment|/**      * @param name the name to set      */
specifier|public
name|void
name|setName
parameter_list|(
name|String
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
comment|/**      * @return the serialNumber      */
specifier|public
name|String
name|getSerialNumber
parameter_list|()
block|{
return|return
name|serialNumber
return|;
block|}
comment|/**      * @param serialNumber the serialNumber to set      */
specifier|public
name|void
name|setSerialNumber
parameter_list|(
name|String
name|serialNumber
parameter_list|)
block|{
name|this
operator|.
name|serialNumber
operator|=
name|serialNumber
expr_stmt|;
block|}
comment|/**      * @return the broken      */
specifier|public
name|boolean
name|isBroken
parameter_list|()
block|{
return|return
name|broken
return|;
block|}
comment|/**      * @param broken the broken to set      */
specifier|public
name|void
name|setBroken
parameter_list|(
name|boolean
name|broken
parameter_list|)
block|{
name|this
operator|.
name|broken
operator|=
name|broken
expr_stmt|;
block|}
comment|/*      * (non-Javadoc)      *       * @see java.lang.Object#equals(java.lang.Object)      */
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
name|boolean
name|ret
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|Widget
condition|)
block|{
name|Widget
name|w
init|=
operator|(
name|Widget
operator|)
name|obj
decl_stmt|;
name|ret
operator|=
operator|new
name|EqualsBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|id
argument_list|,
name|w
operator|.
name|id
argument_list|)
operator|.
name|append
argument_list|(
name|name
argument_list|,
name|w
operator|.
name|name
argument_list|)
operator|.
name|append
argument_list|(
name|serialNumber
argument_list|,
name|w
operator|.
name|serialNumber
argument_list|)
operator|.
name|append
argument_list|(
name|broken
argument_list|,
name|w
operator|.
name|broken
argument_list|)
operator|.
name|isEquals
argument_list|()
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|super
operator|.
name|hashCode
argument_list|()
return|;
block|}
comment|/*      * (non-Javadoc)      *       * @see java.lang.Object#toString()      */
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
operator|new
name|ToStringBuilder
argument_list|(
name|this
argument_list|)
operator|.
name|append
argument_list|(
literal|"id"
argument_list|,
name|id
argument_list|)
operator|.
name|append
argument_list|(
literal|"name"
argument_list|,
name|name
argument_list|)
operator|.
name|append
argument_list|(
literal|"serialNumber"
argument_list|,
name|serialNumber
argument_list|)
operator|.
name|append
argument_list|(
literal|"broken"
argument_list|,
name|broken
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

